import os
import random
import datetime
import string

#data for eandom generator
sql_query = '''INSERT INTO `contact`
(user_id, contact_id)
VALUES'''

#preferences
path="gen/contact.sql"
user_id_min = 1
user_id_max = 50
user_contacts_min = 0
user_contacts_max = 6

#global vars
is_first = True


dir = os.path.dirname(path)
try:
    os.stat(dir)
except:
    os.mkdir(dir)

try:
    file = open(path, 'w')
except:
    print('File error')
    sys.exit(0)

file.write(sql_query + '\n')
for user in range(user_id_min, user_id_max+1):
    all_users = set(range(user_id_min, user_id_max+1))
    all_users.remove(user)
    contacts_count = random.randint(user_contacts_min, user_contacts_max)
    for x in range(0, contacts_count):
        contact = random.choice(list(all_users))
        all_users.remove(contact)
        nextline='('+str(user) + ', ' + str(contact) + ')';
        if not is_first:
            nextline = ',\n' + nextline
        else:
            is_first = False
        file.write(nextline)
file.write(';')
file.close()
