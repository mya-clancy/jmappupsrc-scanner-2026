import os

html = """<!DOCTYPE html>
<html lang="en" class="light">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>JMAP PUP Santa Rosa — Admin & Partner Portal</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <script>
    tailwind.config = {
      darkMode: 'class',
      theme: {
        extend: {
          colors: {
            pup: {
              maroon: '#800000',
              maroonDark: '#4A0000',
              maroonDeep: '#280202',
              gold: '#FFC107',
              goldDark: '#D39E00',
              darkBg: '#180303',
              darkSurface: '#260808',
              darkCard: '#360D0D'
            }
          }
        }
      }
    }
  </script>
  <script src="https://unpkg.com/html5-qrcode"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/qrious/4.0.2/qrious.min.js"></script>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <style>
    body { font-family: system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; }
    
    ::-webkit-scrollbar { width: 6px; height: 6px; }
    ::-webkit-scrollbar-track { background: transparent; }
    ::-webkit-scrollbar-thumb { background: rgba(128, 0, 0, 0.2); border-radius: 999px; }
    .dark ::-webkit-scrollbar-thumb { background: rgba(255, 193, 7, 0.2); }

    /* ID PASS CARD EXACT REPLICA DESIGN (IMAGE 3) */
    .id-pass-card {
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
    }

    @media print {
      body * { visibility: hidden; }
      #printable-id-card, #printable-id-card * { visibility: visible; }
      #printable-id-card { position: absolute; left: 0; top: 0; width: 100%; }
    }
  </style>
</head>
<body class="bg-slate-100 text-slate-800 dark:bg-pup-darkBg dark:text-slate-100 min-h-screen transition-colors duration-200">

  <!-- ==================== LOGIN SCREEN ==================== -->
  <div id="screen-login" class="min-h-screen flex flex-col justify-center items-center p-4 sm:p-6 bg-slate-100 dark:bg-pup-darkBg">
    <div class="max-w-md w-full bg-white dark:bg-pup-darkSurface rounded-3xl shadow-xl border border-slate-200 dark:border-red-950/50 p-6 sm:p-8 relative overflow-hidden">
      
      <!-- Top Decorative Accent -->
      <div class="absolute top-0 left-0 right-0 h-3 bg-gradient-to-r from-pup-maroon via-pup-gold to-pup-maroon"></div>

      <!-- Theme Switcher in Login -->
      <button onclick="toggleDarkMode()" class="absolute top-5 right-5 w-9 h-9 rounded-full bg-slate-100 dark:bg-pup-darkCard flex items-center justify-center text-slate-600 dark:text-pup-gold hover:scale-105 transition">
        <i class="fa-solid fa-moon dark:hidden"></i>
        <i class="fa-solid fa-sun hidden dark:block"></i>
      </button>

      <!-- Brand Header -->
      <div class="flex flex-col items-center text-center mt-2 mb-6">
        <div class="w-20 h-20 rounded-full bg-pup-gold flex items-center justify-center font-extrabold text-pup-maroon text-2xl border-4 border-white dark:border-pup-darkCard shadow-lg mb-3">
          PUP
        </div>
        <h1 class="text-xl font-bold text-pup-maroon dark:text-pup-gold tracking-wide">JMAP PUP SANTA ROSA</h1>
        <p class="text-xs text-slate-500 dark:text-slate-400 font-medium mt-1">Official Cloud Web Management Portal</p>
      </div>

      <!-- Login Role Selector -->
      <div class="bg-slate-100 dark:bg-pup-darkCard p-1 rounded-2xl flex text-xs font-bold mb-6">
        <button id="btn-login-admin" onclick="switchLoginMode('admin')" class="flex-1 py-2.5 rounded-xl bg-pup-maroon text-white shadow-sm transition">
          <i class="fa-solid fa-user-shield mr-1"></i> Admin Portal
        </button>
        <button id="btn-login-partner" onclick="switchLoginMode('partner')" class="flex-1 py-2.5 rounded-xl text-slate-600 dark:text-slate-300 transition">
          <i class="fa-solid fa-store mr-1"></i> Partner Cashier
        </button>
      </div>

      <!-- Form: Admin (NO PRE-FILLED DEFAULT VALUES) -->
      <form id="form-admin" onsubmit="handleAdminLogin(event)" class="space-y-4">
        <div>
          <label class="block text-xs font-bold text-slate-600 dark:text-slate-300 mb-1 uppercase tracking-wider">Username</label>
          <div class="relative">
            <i class="fa-solid fa-user absolute left-3.5 top-3.5 text-slate-400 text-xs"></i>
            <input type="text" id="admin-user" required value="" placeholder="Enter username" class="w-full pl-9 pr-4 py-2.5 rounded-xl border border-slate-300 dark:border-red-900/40 bg-white dark:bg-pup-darkCard text-sm focus:outline-none focus:ring-2 focus:ring-pup-maroon dark:focus:ring-pup-gold">
          </div>
        </div>
        <div>
          <label class="block text-xs font-bold text-slate-600 dark:text-slate-300 mb-1 uppercase tracking-wider">Password</label>
          <div class="relative">
            <i class="fa-solid fa-lock absolute left-3.5 top-3.5 text-slate-400 text-xs"></i>
            <input type="password" id="admin-pass" required value="" placeholder="Enter password" class="w-full pl-9 pr-4 py-2.5 rounded-xl border border-slate-300 dark:border-red-900/40 bg-white dark:bg-pup-darkCard text-sm focus:outline-none focus:ring-2 focus:ring-pup-maroon dark:focus:ring-pup-gold">
          </div>
        </div>
        <div id="admin-error" class="hidden text-xs text-red-600 dark:text-red-400 font-semibold bg-red-50 dark:bg-red-950/50 p-3 rounded-xl border border-red-200 dark:border-red-900"></div>
        <button type="submit" class="w-full bg-pup-maroon hover:bg-red-900 text-white font-bold py-3 rounded-xl shadow-lg transition flex items-center justify-center gap-2 text-sm">
          <i class="fa-solid fa-right-to-bracket"></i> Login as Administrator
        </button>
      </form>

      <!-- Form: Partner Cashier (NO PRE-FILLED DEFAULT VALUES) -->
      <form id="form-partner" onsubmit="handlePartnerLogin(event)" class="space-y-4 hidden">
        <div>
          <label class="block text-xs font-bold text-slate-600 dark:text-slate-300 mb-1 uppercase tracking-wider">Select Partner Business</label>
          <select id="partner-select" required class="w-full px-4 py-2.5 rounded-xl border border-slate-300 dark:border-red-900/40 bg-white dark:bg-pup-darkCard text-sm focus:outline-none focus:ring-2 focus:ring-pup-gold">
            <option value="">Select a Partner Store...</option>
          </select>
        </div>
        <div>
          <label class="block text-xs font-bold text-slate-600 dark:text-slate-300 mb-1 uppercase tracking-wider">4-Digit Access PIN</label>
          <input type="password" id="partner-pin" maxlength="4" value="" placeholder="PIN" required class="w-full px-4 py-2.5 rounded-xl border border-slate-300 dark:border-red-900/40 bg-white dark:bg-pup-darkCard text-lg text-center font-mono tracking-[0.5em] focus:outline-none focus:ring-2 focus:ring-pup-gold">
        </div>
        <div id="partner-error" class="hidden text-xs text-red-600 dark:text-red-400 font-semibold bg-red-50 dark:bg-red-950/50 p-3 rounded-xl border border-red-200 dark:border-red-900"></div>
        <button type="submit" class="w-full bg-pup-gold hover:bg-yellow-500 text-pup-maroon font-bold py-3 rounded-xl shadow-lg transition flex items-center justify-center gap-2 text-sm">
          <i class="fa-solid fa-qrcode"></i> Unlock Cashier Scanner
        </button>
      </form>

      <div class="mt-6 text-center text-xs text-slate-400">
        Connected directly to <span class="font-semibold text-pup-maroon dark:text-pup-gold">Firebase Firestore</span>
      </div>
    </div>
  </div>


  <!-- ==================== MAIN DASHBOARD LAYOUT ==================== -->
  <div id="screen-main" class="hidden min-h-screen flex flex-col">
    
    <!-- Top Navigation Header -->
    <header class="bg-pup-maroon dark:bg-pup-maroonDeep text-white border-b border-red-900/50 shadow-md sticky top-0 z-40">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        
        <!-- Brand / Title -->
        <div class="flex items-center gap-3">
          <div class="w-10 h-10 rounded-full bg-pup-gold flex items-center justify-center font-black text-pup-maroon text-sm border-2 border-white shadow">
            PUP
          </div>
          <div>
            <h1 class="font-extrabold text-base sm:text-lg tracking-wide text-white">JMAP PUP Santa Rosa</h1>
            <p class="text-[11px] text-yellow-200 font-medium hidden sm:block">Membership Validation & Partner Management</p>
          </div>
        </div>

        <!-- Role Badge & Controls -->
        <div class="flex items-center gap-3">
          <!-- Role Badge -->
          <div id="user-role-badge" class="px-3 py-1 rounded-full text-xs font-bold bg-red-950 text-yellow-200 border border-red-800 flex items-center gap-1.5 shadow-inner">
            <i class="fa-solid fa-user-shield"></i> <span id="user-role-text">Administrator</span>
          </div>

          <!-- Dark Mode Toggle -->
          <button onclick="toggleDarkMode()" class="w-9 h-9 rounded-xl bg-red-950/80 hover:bg-red-900 border border-red-800 text-yellow-300 flex items-center justify-center transition">
            <i class="fa-solid fa-moon dark:hidden"></i>
            <i class="fa-solid fa-sun hidden dark:block"></i>
          </button>

          <!-- Logout -->
          <button onclick="logout()" class="px-3 py-1.5 rounded-xl bg-red-900/60 hover:bg-red-800 border border-red-700 text-white text-xs font-bold transition flex items-center gap-1.5">
            <i class="fa-solid fa-right-from-bracket"></i> <span class="hidden sm:inline">Logout</span>
          </button>
        </div>
      </div>
    </header>

    <!-- Navigation Tabs Bar -->
    <nav class="bg-white dark:bg-pup-darkSurface border-b border-slate-200 dark:border-red-950/60 shadow-sm sticky top-16 z-30">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex space-x-1 sm:space-x-4 overflow-x-auto py-2 text-xs sm:text-sm font-bold no-scrollbar">
          
          <button onclick="switchTab('roster')" id="nav-tab-roster" class="nav-btn px-4 py-2.5 rounded-xl flex items-center gap-2 transition text-pup-maroon dark:text-pup-gold bg-slate-100 dark:bg-pup-darkCard">
            <i class="fa-solid fa-users text-base"></i> Student Directory
          </button>

          <button onclick="switchTab('scanner')" id="nav-tab-scanner" class="nav-btn px-4 py-2.5 rounded-xl flex items-center gap-2 transition text-slate-500 hover:text-pup-maroon dark:text-slate-400 dark:hover:text-pup-gold">
            <i class="fa-solid fa-qrcode text-base"></i> Web Camera Scanner
          </button>

          <button onclick="switchTab('scans')" id="nav-tab-scans" class="nav-btn px-4 py-2.5 rounded-xl flex items-center gap-2 transition text-slate-500 hover:text-pup-maroon dark:text-slate-400 dark:hover:text-pup-gold admin-only">
            <i class="fa-solid fa-list-check text-base"></i> Live Scan Logs
          </button>

          <button onclick="switchTab('stores')" id="nav-tab-stores" class="nav-btn px-4 py-2.5 rounded-xl flex items-center gap-2 transition text-slate-500 hover:text-pup-maroon dark:text-slate-400 dark:hover:text-pup-gold admin-only">
            <i class="fa-solid fa-store text-base"></i> Partner Stores
          </button>

          <button onclick="switchTab('vouchers')" id="nav-tab-vouchers" class="nav-btn px-4 py-2.5 rounded-xl flex items-center gap-2 transition text-slate-500 hover:text-pup-maroon dark:text-slate-400 dark:hover:text-pup-gold admin-only">
            <i class="fa-solid fa-ticket text-base"></i> Discount Vouchers
          </button>

        </div>
      </div>
    </nav>

    <!-- Main Content Area -->
    <main class="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-6 space-y-6">

      <!-- Stats Metric Cards (Top Overview) -->
      <div id="stats-overview" class="grid grid-cols-2 md:grid-cols-4 gap-4">
        <div class="bg-white dark:bg-pup-darkSurface p-4 rounded-2xl border border-slate-200 dark:border-red-950/60 shadow-sm flex items-center gap-4">
          <div class="w-12 h-12 rounded-2xl bg-red-100 dark:bg-red-950/80 text-pup-maroon dark:text-pup-gold flex items-center justify-center text-xl font-bold">
            <i class="fa-solid fa-graduation-cap"></i>
          </div>
          <div>
            <p class="text-xs text-slate-500 dark:text-slate-400 font-semibold uppercase">Total Students</p>
            <h3 id="stat-total-students" class="text-xl font-extrabold text-slate-800 dark:text-slate-100">0</h3>
          </div>
        </div>

        <div class="bg-white dark:bg-pup-darkSurface p-4 rounded-2xl border border-slate-200 dark:border-red-950/60 shadow-sm flex items-center gap-4">
          <div class="w-12 h-12 rounded-2xl bg-emerald-100 dark:bg-emerald-950/80 text-emerald-600 dark:text-emerald-400 flex items-center justify-center text-xl font-bold">
            <i class="fa-solid fa-user-check"></i>
          </div>
          <div>
            <p class="text-xs text-slate-500 dark:text-slate-400 font-semibold uppercase">Paid Members</p>
            <h3 id="stat-paid-students" class="text-xl font-extrabold text-emerald-600 dark:text-emerald-400">0</h3>
          </div>
        </div>

        <div class="bg-white dark:bg-pup-darkSurface p-4 rounded-2xl border border-slate-200 dark:border-red-950/60 shadow-sm flex items-center gap-4">
          <div class="w-12 h-12 rounded-2xl bg-amber-100 dark:bg-amber-950/80 text-amber-600 dark:text-amber-400 flex items-center justify-center text-xl font-bold">
            <i class="fa-solid fa-store"></i>
          </div>
          <div>
            <p class="text-xs text-slate-500 dark:text-slate-400 font-semibold uppercase">Partner Stores</p>
            <h3 id="stat-total-stores" class="text-xl font-extrabold text-slate-800 dark:text-slate-100">0</h3>
          </div>
        </div>

        <div class="bg-white dark:bg-pup-darkSurface p-4 rounded-2xl border border-slate-200 dark:border-red-950/60 shadow-sm flex items-center gap-4">
          <div class="w-12 h-12 rounded-2xl bg-purple-100 dark:bg-purple-950/80 text-purple-600 dark:text-purple-400 flex items-center justify-center text-xl font-bold">
            <i class="fa-solid fa-bolt"></i>
          </div>
          <div>
            <p class="text-xs text-slate-500 dark:text-slate-400 font-semibold uppercase">Total Scans</p>
            <h3 id="stat-total-scans" class="text-xl font-extrabold text-slate-800 dark:text-slate-100">0</h3>
          </div>
        </div>
      </div>


      <!-- TAB 1: STUDENT DIRECTORY (ROSTER) -->
      <section id="view-roster" class="tab-view space-y-4">
        
        <!-- Action Header -->
        <div class="flex flex-col sm:flex-row justify-between items-stretch sm:items-center gap-3 bg-white dark:bg-pup-darkSurface p-4 rounded-2xl border border-slate-200 dark:border-red-950/60 shadow-sm">
          <div class="relative flex-1">
            <i class="fa-solid fa-magnifying-glass absolute left-3.5 top-3 text-slate-400 text-sm"></i>
            <input type="text" id="search-roster" oninput="renderRoster()" placeholder="Search student name, ID, or department..." class="w-full pl-10 pr-4 py-2 rounded-xl border border-slate-300 dark:border-red-900/40 bg-slate-50 dark:bg-pup-darkCard text-sm focus:outline-none focus:ring-2 focus:ring-pup-maroon dark:focus:ring-pup-gold">
          </div>

          <div class="flex items-center gap-2">
            <select id="filter-payment" onchange="renderRoster()" class="px-3 py-2 rounded-xl border border-slate-300 dark:border-red-900/40 bg-slate-50 dark:bg-pup-darkCard text-xs font-semibold focus:outline-none">
              <option value="ALL">All Statuses</option>
              <option value="PAID">Paid Only</option>
              <option value="PENDING">Pending Only</option>
            </select>

            <button onclick="openStudentModal()" class="px-4 py-2 rounded-xl bg-pup-maroon hover:bg-red-900 text-white font-bold text-xs shadow transition flex items-center justify-center gap-2 whitespace-nowrap">
              <i class="fa-solid fa-user-plus"></i> Add Student
            </button>
          </div>
        </div>

        <!-- Student Roster Grid -->
        <div id="roster-grid" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <!-- Populated dynamically -->
        </div>
      </section>


      <!-- TAB 2: CAMERA SCANNER -->
      <section id="view-scanner" class="tab-view hidden max-w-2xl mx-auto space-y-4">
        <div class="bg-white dark:bg-pup-darkSurface p-6 rounded-3xl border border-slate-200 dark:border-red-950/60 shadow-md text-center">
          
          <div class="flex justify-between items-center mb-4">
            <h2 class="font-extrabold text-lg text-pup-maroon dark:text-pup-gold flex items-center gap-2">
              <i class="fa-solid fa-camera"></i> Live QR Camera Validation
            </h2>
            <button onclick="resetScanner()" class="px-3 py-1 rounded-full bg-slate-100 dark:bg-pup-darkCard text-slate-600 dark:text-slate-300 text-xs font-bold hover:bg-slate-200 transition">
              <i class="fa-solid fa-rotate-right mr-1"></i> Reset
            </button>
          </div>

          <!-- Camera Stream Container -->
          <div id="reader" class="w-full bg-black rounded-2xl overflow-hidden border-2 border-slate-300 dark:border-red-900/60 shadow-inner min-h-[300px]"></div>

          <!-- Scan Result Display -->
          <div id="scan-result" class="hidden mt-6 p-6 rounded-2xl border-2 text-center animate-fade-in transition-all">
            <div id="scan-badge" class="inline-block px-5 py-1.5 rounded-full text-sm font-extrabold mb-3"></div>
            <h3 id="scan-name" class="text-2xl font-black text-slate-800 dark:text-slate-100"></h3>
            <p id="scan-id" class="text-pup-maroon dark:text-pup-gold font-mono font-bold text-sm mt-1"></p>
            <p id="scan-sem" class="text-slate-500 dark:text-slate-400 text-xs mt-2"></p>
          </div>
        </div>
      </section>


      <!-- TAB 3: LIVE SCAN LOGS -->
      <section id="view-scans" class="tab-view hidden space-y-4 admin-only">
        <div class="bg-white dark:bg-pup-darkSurface rounded-2xl border border-slate-200 dark:border-red-950/60 shadow-sm overflow-hidden">
          <div class="p-4 bg-slate-50 dark:bg-pup-darkCard border-b border-slate-200 dark:border-red-950/60 font-bold text-sm text-slate-700 dark:text-slate-200 flex justify-between items-center">
            <span><i class="fa-solid fa-bolt text-pup-gold mr-2"></i> Real-time Entrance & Store Scan History</span>
            <span class="text-xs font-normal text-slate-400">Auto-updates live</span>
          </div>
          <div class="overflow-x-auto">
            <table class="w-full text-left text-xs border-collapse">
              <thead>
                <tr class="bg-slate-100 dark:bg-pup-darkCard/80 text-slate-500 dark:text-slate-400 uppercase font-bold border-b border-slate-200 dark:border-red-950/60">
                  <th class="py-3 px-4">Time</th>
                  <th class="py-3 px-4">Student Name</th>
                  <th class="py-3 px-4">Student ID</th>
                  <th class="py-3 px-4">Status</th>
                  <th class="py-3 px-4">Notes</th>
                </tr>
              </thead>
              <tbody id="scan-logs-table" class="divide-y divide-slate-100 dark:divide-red-950/30 text-slate-700 dark:text-slate-300">
                <!-- Dynamic logs -->
              </tbody>
            </table>
          </div>
        </div>
      </section>


      <!-- TAB 4: PARTNER STORES -->
      <section id="view-stores" class="tab-view hidden space-y-4 admin-only">
        <div class="flex justify-between items-center bg-white dark:bg-pup-darkSurface p-4 rounded-2xl border border-slate-200 dark:border-red-950/60 shadow-sm">
          <h2 class="font-bold text-base text-pup-maroon dark:text-pup-gold flex items-center gap-2">
            <i class="fa-solid fa-store"></i> Participating Partner Stores
          </h2>
          <button onclick="openStoreModal()" class="px-4 py-2 rounded-xl bg-pup-maroon hover:bg-red-900 text-white font-bold text-xs shadow transition flex items-center gap-2">
            <i class="fa-solid fa-plus"></i> Add Partner Store
          </button>
        </div>

        <div id="stores-grid" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <!-- Dynamic Stores -->
        </div>
      </section>


      <!-- TAB 5: DISCOUNT VOUCHERS -->
      <section id="view-vouchers" class="tab-view hidden space-y-4 admin-only">
        <div class="flex justify-between items-center bg-white dark:bg-pup-darkSurface p-4 rounded-2xl border border-slate-200 dark:border-red-950/60 shadow-sm">
          <h2 class="font-bold text-base text-pup-maroon dark:text-pup-gold flex items-center gap-2">
            <i class="fa-solid fa-ticket"></i> Active Discount Vouchers
          </h2>
          <button onclick="openVoucherModal()" class="px-4 py-2 rounded-xl bg-pup-gold hover:bg-yellow-500 text-pup-maroon font-bold text-xs shadow transition flex items-center gap-2">
            <i class="fa-solid fa-plus"></i> Add Discount Voucher
          </button>
        </div>

        <div id="vouchers-grid" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <!-- Dynamic Vouchers -->
        </div>
      </section>

    </main>
  </div>


  <!-- ==================== MEMBER PASS ID CARD VIEW MODAL ==================== -->
  <div id="modal-id-card" class="fixed inset-0 z-50 hidden bg-slate-900/80 backdrop-blur-sm flex items-center justify-center p-4">
    <div class="bg-white dark:bg-pup-darkSurface max-w-3xl w-full rounded-3xl p-6 shadow-2xl border border-slate-200 dark:border-red-900 relative space-y-6 overflow-y-auto max-h-[90vh]">
      
      <!-- Modal Header -->
      <div class="flex justify-between items-center border-b border-slate-200 dark:border-red-950/60 pb-3">
        <h3 class="font-bold text-lg text-pup-maroon dark:text-pup-gold flex items-center gap-2">
          <i class="fa-solid fa-id-card"></i> Official Member Pass ID
        </h3>
        <button onclick="closeModal('modal-id-card')" class="w-8 h-8 rounded-full bg-slate-100 dark:bg-pup-darkCard text-slate-500 dark:text-slate-300 flex items-center justify-center hover:bg-slate-200 transition">
          <i class="fa-solid fa-xmark"></i>
        </button>
      </div>

      <!-- EXACT REPLICA ID PASS CARD MATCHING REFERENCE IMAGE 3 -->
      <div id="printable-id-card" class="id-pass-card">
        
        <!-- Header Banner -->
        <div class="id-pass-header-banner">
          <div class="text-[14px] font-black tracking-wider text-pup-gold uppercase">
            JUNIOR MARKETING ASSOCIATION OF THE PHILIPPINES
          </div>
          <div class="text-[11px] font-bold tracking-wider text-white uppercase mt-0.5">
            PUP SANTA ROSA • OFFICIAL MEMBER PASS
          </div>
        </div>

        <!-- Body Content -->
        <div class="id-pass-body">
          <div class="id-pass-info">
            <div>
              <div class="id-pass-label">MEMBER NAME</div>
              <div id="pass-card-name" class="id-pass-val">Claudette T. Cansanay</div>
            </div>

            <div>
              <div class="id-pass-label">STUDENT ID</div>
              <div id="pass-card-id" class="id-pass-val font-mono">2026-43139-SR-0</div>
            </div>

            <div>
              <div class="id-pass-label">PROGRAM</div>
              <div id="pass-card-dept" class="id-pass-val">BSBA - Marketing Management</div>
            </div>

            <div>
              <div class="id-pass-label">VALID SEMESTER</div>
              <div id="pass-card-badge" class="id-pass-badge">
                <span id="pass-card-sem">2026-2027 | 1st Semester</span> • VALID MEMBER
              </div>
            </div>
          </div>

          <!-- QR Code Container -->
          <div class="id-pass-qr-frame">
            <div class="id-pass-qr-box">
              <canvas id="pass-card-qr" class="w-36 h-36"></canvas>
            </div>
            <span class="id-pass-qr-label">SCAN TO VALIDATE</span>
          </div>
        </div>

        <!-- Footer Bar -->
        <div class="id-pass-footer">
          Polytechnic University of the Philippines • JMAP - PUP Santa Rosa Branch
        </div>
      </div>

      <!-- Action Buttons -->
      <div class="flex justify-end gap-3 border-t border-slate-200 dark:border-red-950/60 pt-4">
        <button onclick="closeModal('modal-id-card')" class="px-5 py-2.5 rounded-xl text-slate-600 dark:text-slate-300 bg-slate-100 dark:bg-pup-darkCard font-bold text-xs hover:bg-slate-200 transition">
          Close
        </button>
        <button onclick="window.print()" class="px-6 py-2.5 rounded-xl bg-pup-gold text-pup-maroon font-black text-xs shadow-md hover:bg-yellow-500 transition flex items-center gap-2">
          <i class="fa-solid fa-print"></i> Print / Save Member Pass
        </button>
      </div>
    </div>
  </div>


  <!-- ==================== ADD / EDIT STUDENT MODAL ==================== -->
  <div id="modal-student" class="fixed inset-0 z-50 hidden bg-slate-900/80 backdrop-blur-sm flex items-center justify-center p-4">
    <div class="bg-white dark:bg-pup-darkSurface max-w-md w-full rounded-3xl p-6 shadow-2xl border border-slate-200 dark:border-red-900 space-y-4">
      <div class="flex justify-between items-center border-b border-slate-200 dark:border-red-950/60 pb-3">
        <h3 id="student-modal-title" class="font-bold text-lg text-pup-maroon dark:text-pup-gold">Add Student</h3>
        <button onclick="closeModal('modal-student')" class="w-8 h-8 rounded-full bg-slate-100 dark:bg-pup-darkCard text-slate-500 dark:text-slate-300 flex items-center justify-center">
          <i class="fa-solid fa-xmark"></i>
        </button>
      </div>

      <form id="form-student" onsubmit="saveStudent(event)" class="space-y-3 text-xs font-semibold">
        <input type="hidden" id="stu-doc-id">
        <div>
          <label class="block mb-1 text-slate-600 dark:text-slate-300">Student ID (PUP Format)</label>
          <input type="text" id="stu-id" required value="" placeholder="e.g. 2026-00101-SR-0" class="w-full p-2.5 rounded-xl border border-slate-300 dark:border-red-900/40 bg-slate-50 dark:bg-pup-darkCard text-sm focus:outline-none">
        </div>
        <div>
          <label class="block mb-1 text-slate-600 dark:text-slate-300">Full Name</label>
          <input type="text" id="stu-name" required value="" placeholder="Full Name" class="w-full p-2.5 rounded-xl border border-slate-300 dark:border-red-900/40 bg-slate-50 dark:bg-pup-darkCard text-sm focus:outline-none">
        </div>
        <div>
          <label class="block mb-1 text-slate-600 dark:text-slate-300">PUP Webmail</label>
          <input type="email" id="stu-email" value="" placeholder="student@iskolarngbayan.pup.edu.ph" class="w-full p-2.5 rounded-xl border border-slate-300 dark:border-red-900/40 bg-slate-50 dark:bg-pup-darkCard text-sm focus:outline-none">
        </div>
        <div>
          <label class="block mb-1 text-slate-600 dark:text-slate-300">Department / Program</label>
          <input type="text" id="stu-dept" value="" placeholder="BSBA - Marketing Management" class="w-full p-2.5 rounded-xl border border-slate-300 dark:border-red-900/40 bg-slate-50 dark:bg-pup-darkCard text-sm focus:outline-none">
        </div>
        <div>
          <label class="block mb-1 text-slate-600 dark:text-slate-300">Active Semester</label>
          <input type="text" id="stu-sem" value="" placeholder="2026-2027 | 1st Semester" class="w-full p-2.5 rounded-xl border border-slate-300 dark:border-red-900/40 bg-slate-50 dark:bg-pup-darkCard text-sm focus:outline-none">
        </div>
        <div class="flex items-center gap-2 pt-2">
          <input type="checkbox" id="stu-paid" class="w-4 h-4 accent-pup-maroon rounded" checked>
          <label for="stu-paid" class="text-sm font-bold text-slate-700 dark:text-slate-200">Membership Dues Paid</label>
        </div>

        <div class="flex justify-end gap-2 border-t border-slate-200 dark:border-red-950/60 pt-4 mt-4">
          <button type="button" onclick="closeModal('modal-student')" class="px-4 py-2 rounded-xl text-slate-600 dark:text-slate-300 bg-slate-100 dark:bg-pup-darkCard">Cancel</button>
          <button type="submit" class="px-5 py-2 rounded-xl bg-pup-maroon text-white font-bold shadow">Save Student</button>
        </div>
      </form>
    </div>
  </div>


  <!-- ==================== ADD STORE MODAL ==================== -->
  <div id="modal-store" class="fixed inset-0 z-50 hidden bg-slate-900/80 backdrop-blur-sm flex items-center justify-center p-4">
    <div class="bg-white dark:bg-pup-darkSurface max-w-md w-full rounded-3xl p-6 shadow-2xl border border-slate-200 dark:border-red-900 space-y-4">
      <div class="flex justify-between items-center border-b border-slate-200 dark:border-red-950/60 pb-3">
        <h3 id="store-modal-title" class="font-bold text-lg text-pup-maroon dark:text-pup-gold">Add Partner Store</h3>
        <button onclick="closeModal('modal-store')" class="w-8 h-8 rounded-full bg-slate-100 dark:bg-pup-darkCard text-slate-500 dark:text-slate-300 flex items-center justify-center">
          <i class="fa-solid fa-xmark"></i>
        </button>
      </div>

      <form id="form-store" onsubmit="saveStore(event)" class="space-y-3 text-xs font-semibold">
        <input type="hidden" id="store-doc-id">
        <div>
          <label class="block mb-1 text-slate-600 dark:text-slate-300">Store Name</label>
          <input type="text" id="store-name" required value="" placeholder="Store Name" class="w-full p-2.5 rounded-xl border border-slate-300 dark:border-red-900/40 bg-slate-50 dark:bg-pup-darkCard text-sm focus:outline-none">
        </div>
        <div>
          <label class="block mb-1 text-slate-600 dark:text-slate-300">Category</label>
          <input type="text" id="store-cat" value="" placeholder="Food & Beverage" class="w-full p-2.5 rounded-xl border border-slate-300 dark:border-red-900/40 bg-slate-50 dark:bg-pup-darkCard text-sm focus:outline-none">
        </div>
        <div>
          <label class="block mb-1 text-slate-600 dark:text-slate-300">4-Digit Access PIN</label>
          <input type="text" id="store-pin" maxlength="4" required value="" placeholder="1234" class="w-full p-2.5 rounded-xl border border-slate-300 dark:border-red-900/40 bg-slate-50 dark:bg-pup-darkCard text-lg text-center font-mono tracking-widest focus:outline-none">
        </div>

        <div class="flex justify-end gap-2 border-t border-slate-200 dark:border-red-950/60 pt-4 mt-4">
          <button type="button" onclick="closeModal('modal-store')" class="px-4 py-2 rounded-xl text-slate-600 dark:text-slate-300 bg-slate-100 dark:bg-pup-darkCard">Cancel</button>
          <button type="submit" class="px-5 py-2 rounded-xl bg-pup-maroon text-white font-bold shadow">Save Store</button>
        </div>
      </form>
    </div>
  </div>


  <!-- ==================== ADD VOUCHER MODAL ==================== -->
  <div id="modal-voucher" class="fixed inset-0 z-50 hidden bg-slate-900/80 backdrop-blur-sm flex items-center justify-center p-4">
    <div class="bg-white dark:bg-pup-darkSurface max-w-md w-full rounded-3xl p-6 shadow-2xl border border-slate-200 dark:border-red-900 space-y-4">
      <div class="flex justify-between items-center border-b border-slate-200 dark:border-red-950/60 pb-3">
        <h3 class="font-bold text-lg text-pup-maroon dark:text-pup-gold">Distribute Discount Voucher</h3>
        <button onclick="closeModal('modal-voucher')" class="w-8 h-8 rounded-full bg-slate-100 dark:bg-pup-darkCard text-slate-500 dark:text-slate-300 flex items-center justify-center">
          <i class="fa-solid fa-xmark"></i>
        </button>
      </div>

      <form id="form-voucher" onsubmit="saveVoucher(event)" class="space-y-3 text-xs font-semibold">
        <div>
          <label class="block mb-1 text-slate-600 dark:text-slate-300">Partner Store</label>
          <select id="vouch-store-select" required class="w-full p-2.5 rounded-xl border border-slate-300 dark:border-red-900/40 bg-slate-50 dark:bg-pup-darkCard text-sm focus:outline-none">
            <!-- Dynamic stores -->
          </select>
        </div>
        <div>
          <label class="block mb-1 text-slate-600 dark:text-slate-300">Discount Title</label>
          <input type="text" id="vouch-title" required value="" placeholder="e.g. 10% Off All Orders" class="w-full p-2.5 rounded-xl border border-slate-300 dark:border-red-900/40 bg-slate-50 dark:bg-pup-darkCard text-sm focus:outline-none">
        </div>
        <div>
          <label class="block mb-1 text-slate-600 dark:text-slate-300">Description</label>
          <textarea id="vouch-desc" rows="2" placeholder="Description of voucher terms" class="w-full p-2.5 rounded-xl border border-slate-300 dark:border-red-900/40 bg-slate-50 dark:bg-pup-darkCard text-sm focus:outline-none"></textarea>
        </div>

        <div class="flex justify-end gap-2 border-t border-slate-200 dark:border-red-950/60 pt-4 mt-4">
          <button type="button" onclick="closeModal('modal-voucher')" class="px-4 py-2 rounded-xl text-slate-600 dark:text-slate-300 bg-slate-100 dark:bg-pup-darkCard">Cancel</button>
          <button type="submit" class="px-5 py-2 rounded-xl bg-pup-gold text-pup-maroon font-bold shadow">Distribute Voucher</button>
        </div>
      </form>
    </div>
  </div>


  <!-- ==================== FIREBASE & APPLICATION LOGIC ==================== -->
  <script type="module">
    import { initializeApp } from "https://www.gstatic.com/firebasejs/10.12.2/firebase-app.js";
    import { getFirestore, collection, onSnapshot, doc, setDoc, deleteDoc } from "https://www.gstatic.com/firebasejs/10.12.2/firebase-firestore.js";

    const firebaseConfig = {
      apiKey: "AIzaSyDPdFPZIfPGIJXdgDdSyxpUW1SJtwFjqWQ",
      authDomain: "pupsrc-jmap-qrcodescanner.firebaseapp.com",
      projectId: "pupsrc-jmap-qrcodescanner",
      storageBucket: "pupsrc-jmap-qrcodescanner.firebasestorage.app",
      messagingSenderId: "574456278067",
      appId: "1:574456278067:android:d24601aada2da8b5e24746"
    };

    const app = initializeApp(firebaseConfig);
    const db = getFirestore(app);
    window.db = db;

    // State Storage
    window.appState = {
      students: [],
      stores: [],
      vouchers: [],
      scanLogs: [],
      userRole: null,
      partnerData: null
    };

    // Firebase Realtime Snapshots
    onSnapshot(collection(db, "students"), snap => {
      window.appState.students = snap.docs.map(d => ({ docId: d.id, ...d.data() }));
      updateStats();
      if (window.activeTab === 'roster') renderRoster();
    });

    onSnapshot(collection(db, "businesses"), snap => {
      window.appState.stores = snap.docs.map(d => ({ docId: d.id, ...d.data() }));
      updateStats();
      populatePartnerDropdowns();
      if (window.activeTab === 'stores') renderStores();
    });

    onSnapshot(collection(db, "vouchers"), snap => {
      window.appState.vouchers = snap.docs.map(d => ({ docId: d.id, ...d.data() }));
      if (window.activeTab === 'vouchers') renderVouchers();
    });

    onSnapshot(collection(db, "scan_logs"), snap => {
      window.appState.scanLogs = snap.docs.map(d => ({ docId: d.id, ...d.data() })).sort((a,b) => (b.scannedAt || 0) - (a.scannedAt || 0));
      updateStats();
      if (window.activeTab === 'scans') renderScanLogs();
    });
  </script>

  <script>
    window.activeTab = 'roster';
    let html5QrcodeScanner = null;
    let qrGenerator = null;

    function toggleDarkMode() {
      const html = document.documentElement;
      if (html.classList.contains('dark')) {
        html.classList.remove('dark');
        localStorage.setItem('theme', 'light');
      } else {
        html.classList.add('dark');
        localStorage.setItem('theme', 'dark');
      }
    }

    if (localStorage.getItem('theme') === 'dark') {
      document.documentElement.classList.add('dark');
    }

    function switchLoginMode(role) {
      const adminBtn = document.getElementById('btn-login-admin');
      const partnerBtn = document.getElementById('btn-login-partner');
      const adminForm = document.getElementById('form-admin');
      const partnerForm = document.getElementById('form-partner');

      if (role === 'admin') {
        adminBtn.className = "flex-1 py-2.5 rounded-xl bg-pup-maroon text-white shadow-sm transition";
        partnerBtn.className = "flex-1 py-2.5 rounded-xl text-slate-600 dark:text-slate-300 transition";
        adminForm.classList.remove('hidden');
        partnerForm.classList.add('hidden');
      } else {
        partnerBtn.className = "flex-1 py-2.5 rounded-xl bg-pup-gold text-pup-maroon font-bold shadow-sm transition";
        adminBtn.className = "flex-1 py-2.5 rounded-xl text-slate-600 dark:text-slate-300 transition";
        partnerForm.classList.remove('hidden');
        adminForm.classList.add('hidden');
      }
    }

    function populatePartnerDropdowns() {
      const partnerSel = document.getElementById('partner-select');
      const vouchSel = document.getElementById('vouch-store-select');
      
      let options = '<option value="">Select a Partner Store...</option>';
      window.appState.stores.forEach(s => {
        options += `<option value="${s.docId}" data-pin="${s.pin}">${s.name}</option>`;
      });

      if (partnerSel) partnerSel.innerHTML = options;
      if (vouchSel) vouchSel.innerHTML = options;
    }

    function handleAdminLogin(e) {
      e.preventDefault();
      const user = document.getElementById('admin-user').value.trim();
      const pass = document.getElementById('admin-pass').value.trim();
      const err = document.getElementById('admin-error');

      if (user === 'admin' && pass === 'jmapup') {
        window.appState.userRole = 'admin';
        startSession();
      } else {
        err.innerText = "Invalid administrator credentials!";
        err.classList.remove('hidden');
      }
    }

    function handlePartnerLogin(e) {
      e.preventDefault();
      const sel = document.getElementById('partner-select');
      const pin = document.getElementById('partner-pin').value.trim();
      const err = document.getElementById('partner-error');

      if (sel.selectedIndex <= 0) {
        err.innerText = "Please select a partner store";
        err.classList.remove('hidden');
        return;
      }

      const opt = sel.options[sel.selectedIndex];
      const realPin = opt.getAttribute('data-pin');

      if (pin === realPin) {
        window.appState.userRole = 'partner';
        window.appState.partnerData = { id: opt.value, name: opt.innerText };
        startSession();
      } else {
        err.innerText = "Incorrect 4-digit PIN!";
        err.classList.remove('hidden');
      }
    }

    function logout() {
      window.appState.userRole = null;
      window.appState.partnerData = null;
      document.getElementById('screen-main').classList.add('hidden');
      document.getElementById('screen-login').classList.remove('hidden');
      document.getElementById('admin-user').value = '';
      document.getElementById('admin-pass').value = '';
      document.getElementById('partner-pin').value = '';
      if (html5QrcodeScanner) {
        html5QrcodeScanner.clear();
        html5QrcodeScanner = null;
      }
    }

    function startSession() {
      document.getElementById('screen-login').classList.add('hidden');
      document.getElementById('screen-main').classList.remove('hidden');

      const adminEls = document.querySelectorAll('.admin-only');
      const roleText = document.getElementById('user-role-text');

      if (window.appState.userRole === 'admin') {
        adminEls.forEach(el => el.classList.remove('hidden'));
        roleText.innerText = "Administrator";
        switchTab('roster');
      } else {
        adminEls.forEach(el => el.classList.add('hidden'));
        roleText.innerText = window.appState.partnerData.name;
        switchTab('scanner');
      }
    }

    function switchTab(tab) {
      window.activeTab = tab;

      document.querySelectorAll('.tab-view').forEach(el => el.classList.add('hidden'));
      document.querySelectorAll('.nav-btn').forEach(btn => {
        btn.className = "nav-btn px-4 py-2.5 rounded-xl flex items-center gap-2 transition text-slate-500 hover:text-pup-maroon dark:text-slate-400 dark:hover:text-pup-gold";
      });

      const targetView = document.getElementById(`view-${tab}`);
      const targetBtn = document.getElementById(`nav-tab-${tab}`);
      
      if (targetView) targetView.classList.remove('hidden');
      if (targetBtn) {
        targetBtn.className = "nav-btn px-4 py-2.5 rounded-xl flex items-center gap-2 transition text-pup-maroon dark:text-pup-gold bg-slate-100 dark:bg-pup-darkCard font-extrabold shadow-sm";
      }

      if (tab === 'roster') renderRoster();
      if (tab === 'scanner') initScanner();
      else if (html5QrcodeScanner) { html5QrcodeScanner.pause(true); }

      if (tab === 'scans') renderScanLogs();
      if (tab === 'stores') renderStores();
      if (tab === 'vouchers') renderVouchers();
    }

    function updateStats() {
      const students = window.appState.students;
      const stores = window.appState.stores;
      const logs = window.appState.scanLogs;

      document.getElementById('stat-total-students').innerText = students.length;
      document.getElementById('stat-paid-students').innerText = students.filter(s => s.isMembershipPaid).length;
      document.getElementById('stat-total-stores').innerText = stores.length;
      document.getElementById('stat-total-scans').innerText = logs.length;
    }

    function renderRoster() {
      const container = document.getElementById('roster-grid');
      const query = document.getElementById('search-roster').value.toLowerCase();
      const paymentFilter = document.getElementById('filter-payment').value;

      let list = window.appState.students;

      if (paymentFilter === 'PAID') list = list.filter(s => s.isMembershipPaid);
      if (paymentFilter === 'PENDING') list = list.filter(s => !s.isMembershipPaid);

      if (query) {
        list = list.filter(s => 
          (s.fullName && s.fullName.toLowerCase().includes(query)) ||
          (s.studentId && s.studentId.toLowerCase().includes(query)) ||
          (s.department && s.department.toLowerCase().includes(query))
        );
      }

      if (list.length === 0) {
        container.innerHTML = `<div class="col-span-full py-12 text-center text-slate-400 font-semibold">No student records found.</div>`;
        return;
      }

      container.innerHTML = list.map(s => {
        const isPaid = s.isMembershipPaid;
        const statusBadge = isPaid
          ? `<span class="px-2.5 py-1 rounded-full text-[10px] font-extrabold bg-emerald-100 dark:bg-emerald-950/80 text-emerald-700 dark:text-emerald-400"><i class="fa-solid fa-check mr-1"></i> PAID</span>`
          : `<span class="px-2.5 py-1 rounded-full text-[10px] font-extrabold bg-amber-100 dark:bg-amber-950/80 text-amber-700 dark:text-amber-400"><i class="fa-solid fa-clock mr-1"></i> PENDING</span>`;

        return `
          <div class="bg-white dark:bg-pup-darkSurface p-5 rounded-2xl border border-slate-200 dark:border-red-950/60 shadow-sm hover:shadow-md transition flex flex-col justify-between gap-4">
            <div>
              <div class="flex justify-between items-start gap-2 mb-2">
                <div>
                  <h3 class="font-extrabold text-base text-slate-900 dark:text-slate-100">${s.fullName || 'N/A'}</h3>
                  <p class="font-mono text-xs font-bold text-pup-maroon dark:text-pup-gold">${s.studentId || s.docId}</p>
                </div>
                ${statusBadge}
              </div>
              <p class="text-xs text-slate-500 dark:text-slate-400 font-medium">${s.department || 'JMAP Member'}</p>
              <p class="text-[11px] text-slate-400 dark:text-slate-500 mt-1">${s.activeSemester || '2026-2027 | 1st Semester'}</p>
            </div>

            <div class="flex gap-2 border-t border-slate-100 dark:border-red-950/40 pt-3">
              <button onclick="viewMemberPass('${s.docId}')" class="flex-1 py-2 bg-pup-gold hover:bg-yellow-500 text-pup-maroon font-extrabold text-xs rounded-xl shadow-sm transition flex items-center justify-center gap-1.5">
                <i class="fa-solid fa-id-card"></i> Member Pass
              </button>
              <button onclick="editStudent('${s.docId}')" class="px-3 py-2 bg-slate-100 dark:bg-pup-darkCard text-slate-600 dark:text-slate-300 rounded-xl hover:bg-slate-200 transition">
                <i class="fa-solid fa-pen text-xs"></i>
              </button>
              <button onclick="deleteStudent('${s.docId}')" class="px-3 py-2 bg-red-50 dark:bg-red-950/60 text-red-600 dark:text-red-400 rounded-xl hover:bg-red-100 transition">
                <i class="fa-solid fa-trash text-xs"></i>
              </button>
            </div>
          </div>
        `;
      }).join('');
    }

    function viewMemberPass(docId) {
      const student = window.appState.students.find(s => s.docId === docId);
      if (!student) return;

      document.getElementById('pass-card-name').innerText = student.fullName || 'Claudette T. Cansanay';
      document.getElementById('pass-card-id').innerText = student.studentId || student.docId;
      document.getElementById('pass-card-dept').innerText = student.department || 'BSBA - Marketing Management';
      document.getElementById('pass-card-sem').innerText = student.activeSemester || '2026-2027 | 1st Semester';

      const badgeEl = document.getElementById('pass-card-badge');
      if (student.isMembershipPaid) {
        badgeEl.className = "id-pass-badge";
        badgeEl.innerHTML = `<span id="pass-card-sem">${student.activeSemester || '2026-2027 | 1st Semester'}</span> • VALID MEMBER`;
      } else {
        badgeEl.className = "id-pass-badge border-amber-500 bg-amber-500/20 text-amber-300";
        badgeEl.innerHTML = `<span id="pass-card-sem">${student.activeSemester || '2026-2027 | 1st Semester'}</span> • PENDING DUES`;
      }

      const canvas = document.getElementById('pass-card-qr');
      if (!qrGenerator) {
        qrGenerator = new QRious({
          element: canvas,
          size: 200,
          foreground: '#800000',
          background: '#FFFFFF',
          level: 'H'
        });
      }
      qrGenerator.value = JSON.stringify({ studentId: student.studentId || student.docId });

      openModal('modal-id-card');
    }

    function initScanner() {
      document.getElementById('scan-result').classList.add('hidden');
      document.getElementById('reader').classList.remove('hidden');

      if (!html5QrcodeScanner) {
        html5QrcodeScanner = new Html5QrcodeScanner(
          "reader",
          { fps: 10, qrbox: { width: 250, height: 250 }, aspectRatio: 1.0 },
          false
        );
        html5QrcodeScanner.render(onScanSuccess, () => {});
      } else {
        html5QrcodeScanner.resume();
      }
    }

    function resetScanner() {
      initScanner();
    }

    async function onScanSuccess(decodedText) {
      if (html5QrcodeScanner) html5QrcodeScanner.pause(true);

      let rawId = decodedText;
      try {
        const parsed = JSON.parse(decodedText);
        if (parsed.studentId) rawId = parsed.studentId;
      } catch(e){}

      document.getElementById('reader').classList.add('hidden');
      const resDiv = document.getElementById('scan-result');
      const badge = document.getElementById('scan-badge');
      const nameEl = document.getElementById('scan-name');
      const idEl = document.getElementById('scan-id');
      const semEl = document.getElementById('scan-sem');

      resDiv.classList.remove('hidden');
      nameEl.innerText = "Verifying Record...";
      idEl.innerText = rawId;
      semEl.innerText = "";
      badge.className = "inline-block px-5 py-1.5 rounded-full text-sm font-extrabold mb-3 bg-slate-200 text-slate-800";
      badge.innerText = "CHECKING FIRESTORE";

      try {
        const { getDoc, doc, setDoc } = await import("https://www.gstatic.com/firebasejs/10.12.2/firebase-firestore.js");
        const docRef = doc(window.db, "students", rawId);
        const snap = await getDoc(docRef);

        let status = "NOT_FOUND";
        let student = null;
        let notes = window.appState.userRole === 'admin' ? "Scanned at Campus Gate" : `Scanned at ${window.appState.partnerData.name}`;

        if (snap.exists()) {
          student = snap.data();
          status = student.isMembershipPaid ? "VALID" : "PAYMENT_PENDING";
        }

        nameEl.innerText = student ? (student.fullName || 'Unknown Student') : "Unregistered Student";
        semEl.innerText = student ? (student.activeSemester || '') : '';

        if (status === "VALID") {
          badge.className = "inline-block px-6 py-2 rounded-full text-base font-black mb-3 bg-emerald-100 text-emerald-800 border-2 border-emerald-400";
          badge.innerText = "VALID ACTIVE MEMBER";
          resDiv.className = "mt-6 p-6 rounded-2xl border-4 border-emerald-500 bg-emerald-50/50 dark:bg-emerald-950/40 text-center animate-fade-in";
        } else if (status === "PAYMENT_PENDING") {
          badge.className = "inline-block px-6 py-2 rounded-full text-base font-black mb-3 bg-amber-100 text-amber-800 border-2 border-amber-400";
          badge.innerText = "PAYMENT PENDING / EXPIRED";
          resDiv.className = "mt-6 p-6 rounded-2xl border-4 border-amber-500 bg-amber-50/50 dark:bg-amber-950/40 text-center animate-fade-in";
        } else {
          badge.className = "inline-block px-6 py-2 rounded-full text-base font-black mb-3 bg-red-100 text-red-800 border-2 border-red-400";
          badge.innerText = "NO RECORD FOUND";
          resDiv.className = "mt-6 p-6 rounded-2xl border-4 border-red-500 bg-red-50/50 dark:bg-red-950/40 text-center animate-fade-in";
        }

        const logId = Date.now().toString();
        await setDoc(doc(window.db, "scan_logs", logId), {
          studentId: rawId,
          studentName: student ? student.fullName : "Unknown",
          scannedSemester: student ? student.activeSemester : "N/A",
          department: student ? student.department : "N/A",
          status: status,
          scannedAt: Date.now(),
          notes: notes
        });

      } catch(err) {
        console.error(err);
        badge.innerText = "CONNECTION ERROR";
      }
    }

    function renderScanLogs() {
      const container = document.getElementById('scan-logs-table');
      const logs = window.appState.scanLogs;

      if (logs.length === 0) {
        container.innerHTML = `<tr><td colspan="5" class="py-8 text-center text-slate-400">No scans logged yet.</td></tr>`;
        return;
      }

      container.innerHTML = logs.map(l => {
        let statusTag = l.status === 'VALID'
          ? `<span class="px-2 py-0.5 rounded font-extrabold bg-emerald-100 text-emerald-800">VALID</span>`
          : (l.status === 'PAYMENT_PENDING'
            ? `<span class="px-2 py-0.5 rounded font-extrabold bg-amber-100 text-amber-800">PENDING</span>`
            : `<span class="px-2 py-0.5 rounded font-extrabold bg-red-100 text-red-800">INVALID</span>`);

        let time = l.scannedAt ? new Date(l.scannedAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : 'N/A';

        return `
          <tr class="hover:bg-slate-50 dark:hover:bg-pup-darkCard/50">
            <td class="py-3 px-4 font-mono text-slate-400">${time}</td>
            <td class="py-3 px-4 font-bold text-slate-800 dark:text-slate-200">${l.studentName || 'N/A'}</td>
            <td class="py-3 px-4 font-mono text-pup-maroon dark:text-pup-gold font-bold">${l.studentId || 'N/A'}</td>
            <td class="py-3 px-4">${statusTag}</td>
            <td class="py-3 px-4 text-slate-500 dark:text-slate-400">${l.notes || ''}</td>
          </tr>
        `;
      }).join('');
    }

    function renderStores() {
      const container = document.getElementById('stores-grid');
      const stores = window.appState.stores;

      if (stores.length === 0) {
        container.innerHTML = `<div class="col-span-full py-8 text-center text-slate-400">No partner stores added yet.</div>`;
        return;
      }

      container.innerHTML = stores.map(s => `
        <div class="bg-white dark:bg-pup-darkSurface p-4 rounded-2xl border border-slate-200 dark:border-red-950/60 shadow-sm flex justify-between items-center">
          <div>
            <h4 class="font-extrabold text-sm text-pup-maroon dark:text-pup-gold">${s.name || 'Store'}</h4>
            <p class="text-xs text-slate-500 dark:text-slate-400">${s.category || 'Partner Store'} • <span class="font-mono font-bold text-slate-700 dark:text-slate-300">PIN: ${s.pin}</span></p>
          </div>
          <button onclick="deleteStore('${s.docId}')" class="p-2 text-red-500 hover:bg-red-50 dark:hover:bg-red-950/40 rounded-xl transition">
            <i class="fa-solid fa-trash text-sm"></i>
          </button>
        </div>
      `).join('');
    }

    function renderVouchers() {
      const container = document.getElementById('vouchers-grid');
      const vouchers = window.appState.vouchers;

      if (vouchers.length === 0) {
        container.innerHTML = `<div class="col-span-full py-8 text-center text-slate-400">No vouchers added yet.</div>`;
        return;
      }

      container.innerHTML = vouchers.map(v => `
        <div class="bg-white dark:bg-pup-darkSurface p-4 rounded-2xl border border-slate-200 dark:border-red-950/60 shadow-sm relative overflow-hidden flex justify-between items-start">
          <div class="absolute left-0 top-0 bottom-0 w-1.5 bg-pup-gold"></div>
          <div class="pl-3">
            <h4 class="font-black text-sm text-slate-800 dark:text-slate-100">${v.title || 'Voucher'}</h4>
            <p class="text-xs font-bold text-pup-maroon dark:text-pup-gold mb-1">${v.partnerName || 'Partner Store'}</p>
            <p class="text-xs text-slate-500 dark:text-slate-400">${v.description || ''}</p>
          </div>
          <button onclick="deleteVoucher('${v.docId}')" class="p-2 text-red-500 hover:bg-red-50 dark:hover:bg-red-950/40 rounded-xl transition">
            <i class="fa-solid fa-trash text-sm"></i>
          </button>
        </div>
      `).join('');
    }

    function openModal(id) {
      document.getElementById(id).classList.remove('hidden');
    }
    function closeModal(id) {
      document.getElementById(id).classList.add('hidden');
    }

    function openStudentModal() {
      document.getElementById('stu-doc-id').value = '';
      document.getElementById('stu-id').value = '';
      document.getElementById('stu-name').value = '';
      document.getElementById('stu-email').value = '';
      document.getElementById('stu-dept').value = '';
      document.getElementById('stu-sem').value = '';
      document.getElementById('stu-paid').checked = true;
      document.getElementById('student-modal-title').innerText = "Add Student";
      openModal('modal-student');
    }

    function editStudent(docId) {
      const student = window.appState.students.find(s => s.docId === docId);
      if (!student) return;

      document.getElementById('stu-doc-id').value = student.docId;
      document.getElementById('stu-id').value = student.studentId || student.docId;
      document.getElementById('stu-name').value = student.fullName || '';
      document.getElementById('stu-email').value = student.email || '';
      document.getElementById('stu-dept').value = student.department || '';
      document.getElementById('stu-sem').value = student.activeSemester || '';
      document.getElementById('stu-paid').checked = !!student.isMembershipPaid;

      document.getElementById('student-modal-title').innerText = "Edit Student";
      openModal('modal-student');
    }

    async function saveStudent(e) {
      e.preventDefault();
      const rawId = document.getElementById('stu-id').value.trim();
      const docId = document.getElementById('stu-doc-id').value.trim() || rawId;

      if (!rawId) return alert('Student ID required');

      try {
        const { doc, setDoc } = await import("https://www.gstatic.com/firebasejs/10.12.2/firebase-firestore.js");
        await setDoc(doc(window.db, "students", docId), {
          studentId: rawId,
          fullName: document.getElementById('stu-name').value,
          email: document.getElementById('stu-email').value,
          department: document.getElementById('stu-dept').value || "BSBA - Marketing Management",
          activeSemester: document.getElementById('stu-sem').value || "2026-2027 | 1st Semester",
          isMembershipPaid: document.getElementById('stu-paid').checked,
          updatedAt: Date.now()
        }, { merge: true });

        closeModal('modal-student');
      } catch(err) {
        console.error(err);
        alert('Error saving student record');
      }
    }

    async function deleteStudent(docId) {
      if (!confirm("Are you sure you want to delete this student?")) return;
      try {
        const { doc, deleteDoc } = await import("https://www.gstatic.com/firebasejs/10.12.2/firebase-firestore.js");
        await deleteDoc(doc(window.db, "students", docId));
      } catch(e) { console.error(e); }
    }

    function openStoreModal() {
      document.getElementById('store-doc-id').value = '';
      document.getElementById('store-name').value = '';
      document.getElementById('store-cat').value = '';
      document.getElementById('store-pin').value = '';
      openModal('modal-store');
    }

    async function saveStore(e) {
      e.preventDefault();
      const name = document.getElementById('store-name').value.trim();
      const pin = document.getElementById('store-pin').value.trim();
      if (!name || !pin) return alert('Store Name and PIN required');

      const docId = "biz_" + Date.now();
      try {
        const { doc, setDoc } = await import("https://www.gstatic.com/firebasejs/10.12.2/firebase-firestore.js");
        await setDoc(doc(window.db, "businesses", docId), {
          id: docId,
          name: name,
          category: document.getElementById('store-cat').value || "Partner Store",
          pin: pin
        });
        closeModal('modal-store');
      } catch(e) { console.error(e); }
    }

    async function deleteStore(docId) {
      if (!confirm("Delete partner store?")) return;
      try {
        const { doc, deleteDoc } = await import("https://www.gstatic.com/firebasejs/10.12.2/firebase-firestore.js");
        await deleteDoc(doc(window.db, "businesses", docId));
      } catch(e) { console.error(e); }
    }

    function openVoucherModal() {
      document.getElementById('vouch-title').value = '';
      document.getElementById('vouch-desc').value = '';
      openModal('modal-voucher');
    }

    async function saveVoucher(e) {
      e.preventDefault();
      const sel = document.getElementById('vouch-store-select');
      if (sel.selectedIndex <= 0) return alert('Please select a store');

      const title = document.getElementById('vouch-title').value.trim();
      if (!title) return alert('Voucher title required');

      const storeId = sel.options[sel.selectedIndex].value;
      const storeName = sel.options[sel.selectedIndex].innerText;
      const docId = "vouch_" + Date.now();

      try {
        const { doc, setDoc } = await import("https://www.gstatic.com/firebasejs/10.12.2/firebase-firestore.js");
        await setDoc(doc(window.db, "vouchers", docId), {
          id: docId,
          partnerId: storeId,
          partnerName: storeName,
          title: title,
          description: document.getElementById('vouch-desc').value
        });
        closeModal('modal-voucher');
      } catch(e) { console.error(e); }
    }

    async function deleteVoucher(docId) {
      if (!confirm("Delete voucher?")) return;
      try {
        const { doc, deleteDoc } = await import("https://www.gstatic.com/firebasejs/10.12.2/firebase-firestore.js");
        await deleteDoc(doc(window.db, "vouchers", docId));
      } catch(e) { console.error(e); }
    }

    window.toggleDarkMode = toggleDarkMode;
    window.switchLoginMode = switchLoginMode;
    window.handleAdminLogin = handleAdminLogin;
    window.handlePartnerLogin = handlePartnerLogin;
    window.logout = logout;
    window.switchTab = switchTab;
    window.renderRoster = renderRoster;
    window.viewMemberPass = viewMemberPass;
    window.initScanner = initScanner;
    window.resetScanner = resetScanner;
    window.openModal = openModal;
    window.closeModal = closeModal;
    window.openStudentModal = openStudentModal;
    window.editStudent = editStudent;
    window.saveStudent = saveStudent;
    window.deleteStudent = deleteStudent;
    window.openStoreModal = openStoreModal;
    window.saveStore = saveStore;
    window.deleteStore = deleteStore;
    window.openVoucherModal = openVoucherModal;
    window.saveVoucher = saveVoucher;
    window.deleteVoucher = deleteVoucher;
  </script>
</body>
</html>
"""

with open('web/index.html', 'w') as f:
    f.write(html)

print("Updated web/index.html with exact Card Replica and removed pre-filled text bar default values!")
