import re

with open("web/index.html", "r", encoding="utf-8") as f:
    content = f.read()

# Remove the Drive button
button_pattern = r'\s*<button id="btn-save-drive" onclick="savePassToDrive\(\)".*?</button>'
content = re.sub(button_pattern, '', content, flags=re.DOTALL)

# Remove the JS function
js_pattern = r'\s*async function savePassToDrive\(\) \{.*?\n    \}'
content = re.sub(js_pattern, '', content, flags=re.DOTALL)

with open("web/index.html", "w", encoding="utf-8") as f:
    f.write(content)

print("Removed Google Drive integration")
