import re

with open("web/index.html", "r", encoding="utf-8") as f:
    content = f.read()

# 1. ID card CSS fix
old_id_css = """    .id-pass-card {
      width: 100%;
      max-width: 680px;
      aspect-ratio: 1.586 / 1;
      background: linear-gradient(180deg, #2D0202 0%, #170101 100%);
      border-radius: 20px;
      box-shadow: 0 14px 35px rgba(0, 0, 0, 0.7);
      color: #FFFFFF;
      position: relative;
      overflow: hidden;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      box-sizing: border-box;
      margin: 0 auto;
    }

    .id-pass-header-banner {
      background-color: #800000;
      border-bottom: 3px solid #FFC107;
      padding: 14px 24px;
    }

    .id-pass-body {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 24px;
      flex: 1;
      padding: 18px 28px;
    }

    .id-pass-info {
      flex: 1;
      display: flex;
      flex-direction: column;
      justify-content: space-around;
      height: 100%;
    }

    .id-pass-label {
      font-size: 11px;
      letter-spacing: 0.08em;
      color: #D4AF37;
      font-weight: 700;
      text-transform: uppercase;
      margin-bottom: 2px;
    }

    .id-pass-val {
      font-size: 18px;
      font-weight: 700;
      color: #FFFFFF;
      line-height: 1.2;
    }

    .id-pass-badge {
      display: inline-flex;
      align-items: center;
      padding: 5px 16px;
      border-radius: 999px;
      border: 1.5px solid #10B981;
      background: rgba(16, 185, 129, 0.12);
      color: #34D399;
      font-size: 12.5px;
      font-weight: 700;
      width: fit-content;
    }

    .id-pass-qr-frame {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      gap: 8px;
    }

    .id-pass-qr-box {
      background: #FFFFFF;
      border: 2px solid #FFC107;
      border-radius: 16px;
      padding: 12px;
      box-shadow: 0 6px 20px rgba(0,0,0,0.5);
    }

    .id-pass-qr-label {
      font-size: 10px;
      font-weight: 800;
      color: #FFC107;
      letter-spacing: 0.08em;
      text-transform: uppercase;
    }

    .id-pass-footer {
      border-top: 1px solid rgba(255, 255, 255, 0.15);
      padding: 10px 24px;
      font-size: 10.5px;
      color: rgba(255, 255, 255, 0.75);
      text-align: center;
      letter-spacing: 0.02em;
    }"""

new_id_css = """    .id-pass-card {
      container-type: inline-size;
      width: 100%;
      max-width: 680px;
      aspect-ratio: 1.586 / 1;
      background: linear-gradient(180deg, #2D0202 0%, #170101 100%);
      border-radius: 3cqw;
      box-shadow: 0 2cqw 5cqw rgba(0, 0, 0, 0.7);
      color: #FFFFFF;
      position: relative;
      overflow: hidden;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      box-sizing: border-box;
      margin: 0 auto;
    }

    .id-pass-header-banner {
      background-color: #800000;
      border-bottom: 0.5cqw solid #FFC107;
      padding: 2cqw 3.5cqw;
    }

    .id-pass-header-banner .title {
      font-size: 2.1cqw;
      font-weight: 900;
      letter-spacing: 0.05em;
      color: #FFC107;
      text-transform: uppercase;
    }
    
    .id-pass-header-banner .subtitle {
      font-size: 1.6cqw;
      font-weight: 700;
      letter-spacing: 0.08em;
      color: #FFFFFF;
      text-transform: uppercase;
      margin-top: 0.2cqw;
    }

    .id-pass-body {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 3.5cqw;
      flex: 1;
      padding: 2.6cqw 4cqw;
    }

    .id-pass-info {
      flex: 1;
      display: flex;
      flex-direction: column;
      justify-content: space-around;
      height: 100%;
    }

    .id-pass-label {
      font-size: 1.6cqw;
      letter-spacing: 0.08em;
      color: #D4AF37;
      font-weight: 700;
      text-transform: uppercase;
      margin-bottom: 0.3cqw;
    }

    .id-pass-val {
      font-size: 2.6cqw;
      font-weight: 700;
      color: #FFFFFF;
      line-height: 1.2;
    }

    .id-pass-badge {
      display: inline-flex;
      align-items: center;
      padding: 0.7cqw 2.3cqw;
      border-radius: 999px;
      border: 0.2cqw solid #10B981;
      background: rgba(16, 185, 129, 0.12);
      color: #34D399;
      font-size: 1.8cqw;
      font-weight: 700;
      width: fit-content;
      margin-top: 0.5cqw;
    }

    .id-pass-qr-frame {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      gap: 1.2cqw;
    }

    .id-pass-qr-box {
      background: #FFFFFF;
      border: 0.3cqw solid #FFC107;
      border-radius: 2.3cqw;
      padding: 1.7cqw;
      box-shadow: 0 1cqw 3cqw rgba(0,0,0,0.5);
    }

    .id-pass-qr-box canvas {
      width: 20cqw !important;
      height: 20cqw !important;
    }

    .id-pass-qr-label {
      font-size: 1.5cqw;
      font-weight: 800;
      color: #FFC107;
      letter-spacing: 0.08em;
      text-transform: uppercase;
    }

    .id-pass-footer {
      border-top: 1px solid rgba(255, 255, 255, 0.15);
      padding: 1.5cqw 3.5cqw;
      font-size: 1.5cqw;
      color: rgba(255, 255, 255, 0.75);
      text-align: center;
      letter-spacing: 0.02em;
    }"""
content = content.replace(old_id_css, new_id_css)

# Update Banner DOM structure to match new CSS
old_banner_html = """        <div class="id-pass-header-banner">
          <div class="text-[14px] font-black tracking-wider text-pup-gold uppercase">
            JUNIOR MARKETING ASSOCIATION OF THE PHILIPPINES
          </div>
          <div class="text-[11px] font-bold tracking-wider text-white uppercase mt-0.5">
            PUP SANTA ROSA • OFFICIAL MEMBER PASS
          </div>
        </div>"""
new_banner_html = """        <div class="id-pass-header-banner">
          <div class="title">
            JUNIOR MARKETING ASSOCIATION OF THE PHILIPPINES
          </div>
          <div class="subtitle">
            PUP SANTA ROSA • OFFICIAL MEMBER PASS
          </div>
        </div>"""
content = content.replace(old_banner_html, new_banner_html)

# 2. Fix Brand Header Logo and Text
old_header_title = """        <div class="flex items-center gap-3">
          <div class="w-10 h-10 rounded-full bg-pup-gold flex items-center justify-center font-black text-pup-maroon text-sm border-2 border-white shadow">
            PUP
          </div>
          <div>
            <h1 class="font-extrabold text-base sm:text-lg tracking-wide text-white">JMAP PUP Santa Rosa</h1>
            <p class="text-[11px] text-yellow-200 font-medium hidden sm:block">Membership Validation & Partner Management</p>
          </div>
        </div>"""
new_header_title = """        <div class="flex items-center gap-3 min-w-0">
          <img src="jmap_logo.png" alt="JMAP" class="w-10 h-10 shrink-0 rounded-full border-2 border-white shadow object-cover bg-white" onerror="this.outerHTML='<div class=\\'w-10 h-10 shrink-0 rounded-full bg-pup-gold flex items-center justify-center font-black text-pup-maroon text-xs border-2 border-white shadow\\'>JMAP</div>'">
          <div class="min-w-0">
            <h1 class="font-extrabold text-sm sm:text-lg tracking-wide text-white whitespace-nowrap truncate">JMAP PUP Sta. Rosa</h1>
            <p class="text-[11px] text-yellow-200 font-medium hidden sm:block truncate">Membership Validation & Partner Management</p>
          </div>
        </div>"""
content = content.replace(old_header_title, new_header_title)

# Also fix the login screen logo
old_login_logo = """      <!-- Brand Header -->
      <div class="flex flex-col items-center text-center mt-2 mb-6">
        <div class="w-20 h-20 rounded-full bg-pup-gold flex items-center justify-center font-extrabold text-pup-maroon text-2xl border-4 border-white dark:border-pup-darkCard shadow-lg mb-3">
          PUP
        </div>
        <h1 class="text-xl font-bold text-pup-maroon dark:text-pup-gold tracking-wide">JMAP PUP SANTA ROSA</h1>
        <p class="text-xs text-slate-500 dark:text-slate-400 font-medium mt-1">Official Cloud Web Management Portal</p>
      </div>"""
new_login_logo = """      <!-- Brand Header -->
      <div class="flex flex-col items-center text-center mt-2 mb-6">
        <img src="jmap_logo.png" alt="JMAP" class="w-20 h-20 rounded-full border-4 border-white dark:border-pup-darkCard shadow-lg mb-3 object-cover bg-white" onerror="this.outerHTML='<div class=\\'w-20 h-20 rounded-full bg-pup-gold flex items-center justify-center font-extrabold text-pup-maroon text-2xl border-4 border-white dark:border-pup-darkCard shadow-lg mb-3\\'>JMAP</div>'">
        <h1 class="text-xl font-bold text-pup-maroon dark:text-pup-gold tracking-wide">JMAP PUP STA. ROSA</h1>
        <p class="text-xs text-slate-500 dark:text-slate-400 font-medium mt-1">Official Cloud Web Management Portal</p>
      </div>"""
content = content.replace(old_login_logo, new_login_logo)


# 3. Add Remember Me Checkboxes
old_admin_btn = """        <div id="admin-error" class="hidden text-xs text-red-600 dark:text-red-400 font-semibold bg-red-50 dark:bg-red-950/50 p-3 rounded-xl border border-red-200 dark:border-red-900"></div>
        <button type="submit" class="w-full bg-pup-maroon hover:bg-red-900 text-white font-bold py-3 rounded-xl shadow-lg transition flex items-center justify-center gap-2 text-sm">"""
new_admin_btn = """        <div class="flex items-center gap-2">
          <input type="checkbox" id="admin-remember" class="w-4 h-4 accent-pup-maroon rounded">
          <label for="admin-remember" class="text-xs font-bold text-slate-600 dark:text-slate-300">Remember me</label>
        </div>
        <div id="admin-error" class="hidden text-xs text-red-600 dark:text-red-400 font-semibold bg-red-50 dark:bg-red-950/50 p-3 rounded-xl border border-red-200 dark:border-red-900"></div>
        <button type="submit" class="w-full bg-pup-maroon hover:bg-red-900 text-white font-bold py-3 rounded-xl shadow-lg transition flex items-center justify-center gap-2 text-sm">"""
content = content.replace(old_admin_btn, new_admin_btn)

old_partner_btn = """        <div id="partner-error" class="hidden text-xs text-red-600 dark:text-red-400 font-semibold bg-red-50 dark:bg-red-950/50 p-3 rounded-xl border border-red-200 dark:border-red-900"></div>
        <button type="submit" class="w-full bg-pup-gold hover:bg-yellow-500 text-pup-maroon font-bold py-3 rounded-xl shadow-lg transition flex items-center justify-center gap-2 text-sm">"""
new_partner_btn = """        <div class="flex items-center gap-2">
          <input type="checkbox" id="partner-remember" class="w-4 h-4 accent-pup-gold rounded">
          <label for="partner-remember" class="text-xs font-bold text-slate-600 dark:text-slate-300">Remember me</label>
        </div>
        <div id="partner-error" class="hidden text-xs text-red-600 dark:text-red-400 font-semibold bg-red-50 dark:bg-red-950/50 p-3 rounded-xl border border-red-200 dark:border-red-900"></div>
        <button type="submit" class="w-full bg-pup-gold hover:bg-yellow-500 text-pup-maroon font-bold py-3 rounded-xl shadow-lg transition flex items-center justify-center gap-2 text-sm">"""
content = content.replace(old_partner_btn, new_partner_btn)

# 4. Save sessions on login
old_admin_login = """      if (user === 'admin' && pass === 'jmapup') {
        window.appState.userRole = 'admin';
        startSession();"""
new_admin_login = """      if (user === 'admin' && pass === 'jmapup') {
        window.appState.userRole = 'admin';
        if (document.getElementById('admin-remember').checked) {
          localStorage.setItem('jmap_session_role', 'admin');
        }
        startSession();"""
content = content.replace(old_admin_login, new_admin_login)

old_partner_login = """      if (pin === realPin) {
        window.appState.userRole = 'partner';
        window.appState.partnerData = { id: opt.value, name: opt.innerText };
        startSession();"""
new_partner_login = """      if (pin === realPin) {
        window.appState.userRole = 'partner';
        window.appState.partnerData = { id: opt.value, name: opt.innerText };
        if (document.getElementById('partner-remember').checked) {
          localStorage.setItem('jmap_session_role', 'partner');
          localStorage.setItem('jmap_session_partner_id', opt.value);
          localStorage.setItem('jmap_session_partner_name', opt.innerText);
        }
        startSession();"""
content = content.replace(old_partner_login, new_partner_login)

old_logout = """    function logout() {
      window.appState.userRole = null;"""
new_logout = """    function logout() {
      localStorage.removeItem('jmap_session_role');
      localStorage.removeItem('jmap_session_partner_id');
      localStorage.removeItem('jmap_session_partner_name');
      window.appState.userRole = null;"""
content = content.replace(old_logout, new_logout)


# Add checkSession functionality
check_session_code = """
    function checkSession() {
      const savedRole = localStorage.getItem('jmap_session_role');
      if (savedRole === 'admin') {
        window.appState.userRole = 'admin';
        startSession();
      } else if (savedRole === 'partner') {
        const pId = localStorage.getItem('jmap_session_partner_id');
        const pName = localStorage.getItem('jmap_session_partner_name');
        if (pId && pName) {
          window.appState.userRole = 'partner';
          window.appState.partnerData = { id: pId, name: pName };
          startSession();
        }
      }
    }

    // Call checkSession on load after small delay to ensure stores load
    setTimeout(() => {
        checkSession();
    }, 500);
"""
content = content.replace("window.activeTab = 'roster';", "window.activeTab = 'roster';\n" + check_session_code)

with open("web/index.html", "w", encoding="utf-8") as f:
    f.write(content)

print("Updated index.html successfully")
