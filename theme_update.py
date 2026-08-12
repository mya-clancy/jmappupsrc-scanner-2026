import re

with open("web/index.html", "r", encoding="utf-8") as f:
    content = f.read()

# 1. Update Color Palette for Dark Mode
content = content.replace("darkBg: '#180303'", "darkBg: '#0B1120'")
content = content.replace("darkSurface: '#260808'", "darkSurface: '#151C2C'")
content = content.replace("darkCard: '#360D0D'", "darkCard: '#1F2937'")

# 2. Strip dark red borders/dividers and replace with slate/navy equivalents
content = content.replace("dark:border-red-950/60", "dark:border-slate-700/60")
content = content.replace("dark:border-red-950/50", "dark:border-slate-700/50")
content = content.replace("dark:border-red-900/40", "dark:border-slate-700/40")
content = content.replace("dark:border-red-900/60", "dark:border-slate-700/60")
content = content.replace("dark:border-red-900", "dark:border-slate-700")
content = content.replace("dark:divide-red-950/30", "dark:divide-slate-700/30")
content = content.replace("dark:border-red-950", "dark:border-slate-700")

# 3. Fix specific elements that use red backgrounds for layout in dark mode
content = content.replace("dark:bg-pup-maroonDeep", "dark:bg-pup-darkSurface")

# Header border specifically
content = content.replace("bg-pup-maroon dark:bg-pup-darkSurface text-white border-b border-red-900/50", "bg-pup-maroon dark:bg-pup-darkSurface text-white border-b border-red-900/50 dark:border-slate-800")

# For the role badge (which was red-950), make it darkCard in dark mode to blend better with navy
content = content.replace("bg-red-950 text-yellow-200 border border-red-800", "bg-red-950 dark:bg-pup-darkCard text-yellow-200 border border-red-800 dark:border-slate-700")

# For the Dark mode toggle buttons and Settings buttons in header
content = content.replace("bg-red-950/80 hover:bg-red-900 border border-red-800", "bg-red-950/80 dark:bg-pup-darkCard hover:bg-red-900 dark:hover:bg-slate-700 border border-red-800 dark:border-slate-700")

# Logout button in header
content = content.replace("bg-red-900/60 hover:bg-red-800 border border-red-700", "bg-red-900/60 dark:bg-pup-darkCard hover:bg-red-800 dark:hover:bg-slate-700 border border-red-700 dark:border-slate-700")


with open("web/index.html", "w", encoding="utf-8") as f:
    f.write(content)

print("Updated theme colors successfully")
