import re

with open("web/index.html", "r") as f:
    html = f.read()

# 1. Make tabs responsive
html = html.replace(
    '<div class="max-w-7xl mx-auto px-6 flex space-x-8 text-sm font-semibold">',
    '<div class="max-w-7xl mx-auto px-6 flex space-x-8 text-sm font-semibold overflow-x-auto whitespace-nowrap">'
)

# 2. Add scanner tab
scanner_tab = '''      <button onclick="switchTab('scanner')" id="tab-scanner" class="py-4 border-b-2 border-transparent text-slate-500 hover:text-slate-800 flex items-center gap-2">
        <i class="fa-solid fa-camera"></i> Web Scanner
      </button>'''
html = html.replace(
    '</button>\n    </div>\n  </div>',
    '</button>\n' + scanner_tab + '\n    </div>\n  </div>'
)

# 3. Add html5-qrcode
html = html.replace(
    '<script src="https://cdn.tailwindcss.com"></script>',
    '<script src="https://cdn.tailwindcss.com"></script>\n  <script src="https://unpkg.com/html5-qrcode"></script>'
)

# 4. Add scanner section
scanner_section = '''
    <!-- TAB 5: Web Scanner -->
    <section id="view-scanner" class="hidden">
      <div class="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden p-6 text-center max-w-lg mx-auto">
        <h3 class="text-xl font-bold maroon-text mb-2">Web QR Scanner</h3>
        <p class="text-sm text-slate-500 mb-6">Requesting camera permissions to scan student IDs directly from this device.</p>
        <div id="reader" class="mx-auto w-full max-w-sm rounded overflow-hidden shadow-inner border border-slate-300"></div>
        <div id="scan-result" class="mt-6 hidden">
          <div id="scan-badge" class="px-4 py-2 rounded-xl text-lg font-bold mb-2"></div>
          <p id="scan-name" class="font-bold text-slate-800 text-xl"></p>
          <p id="scan-id" class="text-slate-600 font-mono text-sm"></p>
          <button onclick="resetScanner()" class="mt-4 px-6 py-2 bg-slate-200 hover:bg-slate-300 rounded-xl font-semibold text-slate-700 transition">Scan Next</button>
        </div>
      </div>
    </section>
'''
html = html.replace('  </main>', scanner_section + '  </main>')

# 5. Fix overflow-hidden on tables -> overflow-x-auto
html = html.replace('overflow-hidden', 'overflow-x-auto')
# Wait, this might affect the modal or the scanner. 
# Let's target specific instances:
html = html.replace('class="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-x-auto p-6"', 'class="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden p-6"')
html = html.replace('class="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-x-auto overflow-x-auto p-6 text-center max-w-lg mx-auto"', 'class="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden p-6 text-center max-w-lg mx-auto"')

# Make sure tabs array includes scanner
html = html.replace(
    "['students', 'scans', 'businesses', 'redemptions'].forEach",
    "['students', 'scans', 'businesses', 'redemptions', 'scanner'].forEach"
)

# 6. Add scanner logic
scanner_script = '''
    // Web Scanner Logic
    let html5QrcodeScanner = null;

    function initScanner() {
      if (!html5QrcodeScanner) {
        html5QrcodeScanner = new Html5QrcodeScanner(
          "reader",
          { fps: 10, qrbox: {width: 250, height: 250} },
          /* verbose= */ false);
        html5QrcodeScanner.render(onScanSuccess, onScanFailure);
      }
    }

    async function onScanSuccess(decodedText, decodedResult) {
      if (html5QrcodeScanner) {
        html5QrcodeScanner.pause(true); // Pause scanning
      }
      
      // Attempt to parse standard student payload (or just assume it's an ID)
      let studentId = decodedText;
      try {
        const json = JSON.parse(decodedText);
        if (json.studentId) studentId = json.studentId;
      } catch (e) {}

      document.getElementById('reader').classList.add('hidden');
      const resDiv = document.getElementById('scan-result');
      const badge = document.getElementById('scan-badge');
      const nameEl = document.getElementById('scan-name');
      const idEl = document.getElementById('scan-id');
      
      resDiv.classList.remove('hidden');
      nameEl.innerText = "Checking...";
      idEl.innerText = studentId;
      badge.className = "px-4 py-2 rounded-xl text-lg font-bold mb-2 bg-slate-100 text-slate-800";
      badge.innerText = "LOADING";

      // Query firestore directly
      try {
        const { getDoc, doc, setDoc } = await import("https://www.gstatic.com/firebasejs/10.12.2/firebase-firestore.js");
        const studentDoc = await getDoc(doc(window.db, "students", studentId));
        
        let status = "NOT_FOUND";
        let studentData = null;

        if (studentDoc.exists()) {
          studentData = studentDoc.data();
          if (!studentData.isMembershipPaid) {
            status = "PAYMENT_PENDING";
          } else {
            status = "VALID"; // simplified check
          }
        }

        // Display Result
        if (status === "VALID") {
          badge.className = "px-4 py-2 rounded-xl text-lg font-bold mb-2 bg-emerald-100 text-emerald-800";
          badge.innerText = "VALID";
          nameEl.innerText = studentData.fullName;
        } else if (status === "PAYMENT_PENDING") {
          badge.className = "px-4 py-2 rounded-xl text-lg font-bold mb-2 bg-amber-100 text-amber-800";
          badge.innerText = "PENDING DUES";
          nameEl.innerText = studentData.fullName;
        } else {
          badge.className = "px-4 py-2 rounded-xl text-lg font-bold mb-2 bg-red-100 text-red-800";
          badge.innerText = "NOT FOUND";
          nameEl.innerText = "Unregistered Student";
        }

        // Log it!
        const logId = Date.now().toString() + "-" + Math.floor(Math.random()*1000);
        await setDoc(doc(window.db, "scan_logs", logId), {
          studentId: studentId,
          studentName: studentData ? studentData.fullName : "Unregistered Student",
          scannedSemester: studentData ? studentData.activeSemester : "N/A",
          department: studentData ? studentData.department : "N/A",
          status: status,
          scannedAt: Date.now(),
          notes: "Scanned via Web Scanner"
        });

      } catch (err) {
        console.error(err);
        badge.innerText = "ERROR";
      }
    }

    function onScanFailure(error) {
      // ignore frame failures
    }

    window.resetScanner = () => {
      document.getElementById('scan-result').classList.add('hidden');
      document.getElementById('reader').classList.remove('hidden');
      if (html5QrcodeScanner) {
        html5QrcodeScanner.resume();
      }
    };

    // Override switchTab to init scanner when opened
    const oldSwitchTab = window.switchTab;
    window.switchTab = function(tab) {
      ['students', 'scans', 'businesses', 'redemptions', 'scanner'].forEach(t => {
        document.getElementById(`view-${t}`).classList.add('hidden');
        document.getElementById(`tab-${t}`).className = 'py-4 border-b-2 border-transparent text-slate-500 hover:text-slate-800 flex items-center gap-2';
      });
      document.getElementById(`view-${tab}`).classList.remove('hidden');
      document.getElementById(`tab-${tab}`).className = 'py-4 border-b-2 border-red-800 text-red-900 flex items-center gap-2 font-bold';

      if (tab === 'scanner') {
        initScanner();
      }
    };
'''

html = html.replace('  </script>\n</body>', scanner_script + '\n  </script>\n</body>')
html = html.replace('const db = getFirestore(app);', 'const db = getFirestore(app);\n    window.db = db;')

with open("web/index.html", "w") as f:
    f.write(html)
