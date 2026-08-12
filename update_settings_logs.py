import re

with open("web/index.html", "r", encoding="utf-8") as f:
    content = f.read()

# 1. Update Header to add Settings button
old_header_controls = '''          <!-- Dark Mode Toggle -->
          <button onclick="toggleDarkMode()" class="w-9 h-9 rounded-xl bg-red-950/80 hover:bg-red-900 border border-red-800 text-yellow-300 flex items-center justify-center transition">
            <i class="fa-solid fa-moon dark:hidden"></i>
            <i class="fa-solid fa-sun hidden dark:block"></i>
          </button>

          <!-- Logout -->
          <button onclick="logout()" class="px-3 py-1.5 rounded-xl bg-red-900/60 hover:bg-red-800 border border-red-700 text-white text-xs font-bold transition flex items-center gap-1.5">
            <i class="fa-solid fa-right-from-bracket"></i> <span class="hidden sm:inline">Logout</span>
          </button>'''

new_header_controls = '''          <!-- Dark Mode Toggle -->
          <button onclick="toggleDarkMode()" class="w-9 h-9 rounded-xl bg-red-950/80 hover:bg-red-900 border border-red-800 text-yellow-300 flex items-center justify-center transition" title="Toggle Theme">
            <i class="fa-solid fa-moon dark:hidden"></i>
            <i class="fa-solid fa-sun hidden dark:block"></i>
          </button>

          <!-- Settings Button -->
          <button onclick="openModal('modal-settings')" class="w-9 h-9 rounded-xl bg-red-950/80 hover:bg-red-900 border border-red-800 text-yellow-300 flex items-center justify-center transition" title="Settings & DB Status">
            <i class="fa-solid fa-gear"></i>
          </button>

          <!-- Logout Button -->
          <button onclick="logout()" class="px-3 py-1.5 rounded-xl bg-red-900/60 hover:bg-red-800 border border-red-700 text-white text-xs font-bold transition flex items-center gap-1.5" title="Logout">
            <i class="fa-solid fa-right-from-bracket"></i> <span class="hidden sm:inline">Logout</span>
          </button>'''

if old_header_controls in content:
    content = content.replace(old_header_controls, new_header_controls)
    print("Updated Header Controls")
else:
    print("Header controls pattern not found")

# 2. Update Total Scans Stat Card to include Clear Logs icon button
old_total_scans_card = '''        <div class="bg-white dark:bg-pup-darkSurface p-4 rounded-2xl border border-slate-200 dark:border-red-950/60 shadow-sm flex items-center gap-4">
          <div class="w-12 h-12 rounded-2xl bg-purple-100 dark:bg-purple-950/80 text-purple-600 dark:text-purple-400 flex items-center justify-center text-xl font-bold">
            <i class="fa-solid fa-bolt"></i>
          </div>
          <div>
            <p class="text-xs text-slate-500 dark:text-slate-400 font-semibold uppercase">Total Scans</p>
            <h3 id="stat-total-scans" class="text-xl font-extrabold text-slate-800 dark:text-slate-100">0</h3>
          </div>
        </div>'''

new_total_scans_card = '''        <div class="bg-white dark:bg-pup-darkSurface p-4 rounded-2xl border border-slate-200 dark:border-red-950/60 shadow-sm flex items-center justify-between gap-3">
          <div class="flex items-center gap-3 min-w-0">
            <div class="w-12 h-12 shrink-0 rounded-2xl bg-purple-100 dark:bg-purple-950/80 text-purple-600 dark:text-purple-400 flex items-center justify-center text-xl font-bold">
              <i class="fa-solid fa-bolt"></i>
            </div>
            <div class="truncate">
              <p class="text-xs text-slate-500 dark:text-slate-400 font-semibold uppercase">Total Scans</p>
              <h3 id="stat-total-scans" class="text-xl font-extrabold text-slate-800 dark:text-slate-100">0</h3>
            </div>
          </div>
          <button onclick="confirmClearScanLogs()" title="Clear All Scan Logs" class="shrink-0 p-2.5 rounded-xl bg-slate-100 dark:bg-pup-darkCard text-slate-400 hover:text-red-600 hover:bg-red-50 dark:hover:bg-red-950/80 dark:hover:text-red-400 border border-slate-200 dark:border-red-900/40 transition admin-only">
            <i class="fa-solid fa-trash-can text-sm"></i>
          </button>
        </div>'''

if old_total_scans_card in content:
    content = content.replace(old_total_scans_card, new_total_scans_card)
    print("Updated Total Scans Card with Clear Logs button")
else:
    print("Total scans card pattern not found")

# 3. Add Settings Modal
settings_modal = '''
  <!-- ==================== SETTINGS & DB STATUS MODAL ==================== -->
  <div id="modal-settings" class="fixed inset-0 z-50 hidden bg-slate-900/80 backdrop-blur-sm flex items-center justify-center p-4">
    <div class="bg-white dark:bg-pup-darkSurface max-w-md w-full rounded-3xl p-6 shadow-2xl border border-slate-200 dark:border-red-900 space-y-5">
      
      <div class="flex justify-between items-center border-b border-slate-200 dark:border-red-950/60 pb-3">
        <h3 class="font-bold text-lg text-pup-maroon dark:text-pup-gold flex items-center gap-2">
          <i class="fa-solid fa-gear"></i> Settings & Database Status
        </h3>
        <button onclick="closeModal('modal-settings')" class="w-8 h-8 rounded-full bg-slate-100 dark:bg-pup-darkCard text-slate-500 dark:text-slate-300 flex items-center justify-center hover:bg-slate-200 transition">
          <i class="fa-solid fa-xmark"></i>
        </button>
      </div>

      <!-- Firebase DB Status Card -->
      <div class="bg-slate-50 dark:bg-pup-darkCard p-4 rounded-2xl border border-slate-200 dark:border-red-900/40 space-y-3">
        <div class="flex items-center justify-between">
          <span class="text-xs font-bold text-slate-600 dark:text-slate-300 uppercase tracking-wider">Firebase Firestore</span>
          <div id="db-status-badge" class="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-extrabold bg-emerald-100 text-emerald-700 dark:bg-emerald-950/80 dark:text-emerald-400 border border-emerald-300 dark:border-emerald-800">
            <span class="w-2 h-2 rounded-full bg-emerald-500 animate-pulse"></span>
            <span id="db-status-text">Active / Connected</span>
          </div>
        </div>

        <div class="text-xs space-y-1.5 text-slate-500 dark:text-slate-400 font-medium pt-1">
          <div class="flex justify-between">
            <span>Project ID:</span>
            <span class="font-mono text-slate-800 dark:text-slate-200 font-bold">pupsrc-jmap-qrcodescanner</span>
          </div>
          <div class="flex justify-between">
            <span>Sync Engine:</span>
            <span class="text-emerald-600 dark:text-emerald-400 font-bold">Realtime Web Listeners Active</span>
          </div>
          <div class="flex justify-between">
            <span>Current Role:</span>
            <span id="settings-user-role" class="font-bold text-pup-maroon dark:text-pup-gold">Administrator</span>
          </div>
        </div>
      </div>

      <!-- Quick Actions -->
      <div class="pt-2 border-t border-slate-200 dark:border-red-950/60 space-y-2">
        <button onclick="confirmClearScanLogs(); closeModal('modal-settings');" class="w-full bg-slate-100 dark:bg-pup-darkCard hover:bg-red-50 dark:hover:bg-red-950/60 text-slate-700 dark:text-slate-200 hover:text-red-600 dark:hover:text-red-400 font-bold py-2.5 rounded-xl border border-slate-200 dark:border-red-900/40 transition flex items-center justify-center gap-2 text-xs uppercase tracking-wider admin-only">
          <i class="fa-solid fa-trash-can"></i> Clear All Scan Logs
        </button>

        <button onclick="logout(); closeModal('modal-settings');" class="w-full bg-pup-maroon hover:bg-red-900 text-white font-bold py-3 rounded-xl shadow-md transition flex items-center justify-center gap-2 text-xs uppercase tracking-wider">
          <i class="fa-solid fa-right-from-bracket"></i> Logout from Portal
        </button>
      </div>

    </div>
  </div>
'''

# Insert modal before the module script
if '<script type="module">' in content:
    content = content.replace('<script type="module">', settings_modal + '\n  <script type="module">')
    print("Inserted Settings Modal")
else:
    print("Script type module not found")

# 4. Add JS function for confirmClearScanLogs and update user role in settings
js_addon = '''
    async function confirmClearScanLogs() {
      if (!window.appState.scanLogs || window.appState.scanLogs.length === 0) {
        alert("There are no scan logs to clear.");
        return;
      }
      if (!confirm(`Are you sure you want to clear all ${window.appState.scanLogs.length} scan log records? This action cannot be undone.`)) {
        return;
      }
      try {
        const { doc, deleteDoc } = await import("https://www.gstatic.com/firebasejs/10.12.2/firebase-firestore.js");
        const logs = [...window.appState.scanLogs];
        for (const log of logs) {
          if (log.docId) {
            await deleteDoc(doc(window.db, "scan_logs", log.docId));
          }
        }
        alert("All scan logs have been cleared successfully.");
      } catch (err) {
        console.error("Error clearing scan logs:", err);
        alert("Failed to clear scan logs: " + err.message);
      }
    }

    window.confirmClearScanLogs = confirmClearScanLogs;
'''

content = content.replace('window.deleteVoucher = deleteVoucher;', 'window.deleteVoucher = deleteVoucher;\n' + js_addon)

# Also update startSession to populate role in settings modal
old_start_session = '''      if (window.appState.userRole === 'admin') {
        adminEls.forEach(el => el.classList.remove('hidden'));
        roleText.innerText = "Administrator";
        switchTab('roster');
      } else {
        adminEls.forEach(el => el.classList.add('hidden'));
        roleText.innerText = window.appState.partnerData.name;
        switchTab('scanner');
      }'''

new_start_session = '''      const settingsRoleText = document.getElementById('settings-user-role');

      if (window.appState.userRole === 'admin') {
        adminEls.forEach(el => el.classList.remove('hidden'));
        roleText.innerText = "Administrator";
        if (settingsRoleText) settingsRoleText.innerText = "Administrator";
        switchTab('roster');
      } else {
        adminEls.forEach(el => el.classList.add('hidden'));
        roleText.innerText = window.appState.partnerData.name;
        if (settingsRoleText) settingsRoleText.innerText = window.appState.partnerData.name;
        switchTab('scanner');
      }'''

if old_start_session in content:
    content = content.replace(old_start_session, new_start_session)
    print("Updated startSession for settings role display")

with open("web/index.html", "w", encoding="utf-8") as f:
    f.write(content)

print("Finished updating web/index.html")
