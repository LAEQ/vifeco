import json

f = open('messages.json')

values = json.load(f)

english = []
french = []
spanish = []

for value in values:
    for k, v in value.items():
        english.append("{} : {}".format(k, v['en-CA']))
        french.append("{} : {}".format(k, v['fr-CA']))
        spanish.append("{} : {}".format(k, v['es-ES']))

english.sort()
french.sort()
spanish.sort()

with open('messages_en_CA.properties', 'w') as f:
    for item in english:
        f.write("%s\n" % item)

with open('messages_fr_CA.properties', 'w') as f:
    for item in french:
        f.write("%s\n" % item)

with open('messages_es_ES.properties', 'w') as f:
    for item in spanish:
        f.write("%s\n" % item)
