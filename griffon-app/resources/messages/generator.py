import json
import io

# Parse english keys
f = open("keys.txt", encoding="utf-8")
lines = [l for l in (line.strip() for line in f) if (l and "-" not in l)]

result = []

for l in lines:
    words = l.split("=")
    key = words[0].strip()
    message = words[1].strip()
    dict = {}
    dict[key] = {'en-CA': message}
    result.append(dict)

# Parse french keys
f = open("keys_fr.txt", encoding="utf-8")
lines = [l for l in (line.strip() for line in f) if (l and "-" not in l)]

for l in lines:
    words = l.split("=")
    key = words[0].strip()
    message = words[1].strip()

    for r in result:
        if key in r.keys():
            r[key]['fr-CA'] = message



y = json.dumps(result, ensure_ascii=False, indent=4, sort_keys=True)
f = io.open("messages.json", "w", encoding='utf8')
f.write(y)
f.close