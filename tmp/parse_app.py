import urllib.request
import re

url = 'https://nocturne-learning-lounge.lovable.app/assets/routes-B9V0wGFi.js'
req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0'})
content = urllib.request.urlopen(req).read().decode('utf-8', errors='ignore')

# Search for matches
pattern = re.compile(r'(`[^`\n]{3,60}`|"([^"\n]{3,60})")')
matches = pattern.findall(content)
ui_texts = set()
keywords = ['learning', 'schedule', 'Weekly', 'Quick', 'tools', 'Streak', 'Overview', 'Focus', 'Midnight', 'Target', 'Breakthrough', 'Chemistry', 'flashcards', 'courses', 'Tasks', 'Alex', 'lessons', 'min', 'hour', 'Stat', 'Session', 'progress']
for m in matches:
    s = m[0].strip('`"')
    if any(k.lower() in s.lower() for k in keywords):
        ui_texts.add(s)

print(f"Total matching UI texts: {len(ui_texts)}")
for s in sorted(ui_texts)[:80]:
    print("  *", s)
