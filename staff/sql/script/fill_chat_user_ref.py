import os
import random
import datetime
import string

#data for eandom generator
sql_query = '''INSERT INTO `chat_user_ref`
(`chat_id`, `user_id`)
VALUES
'''

#preferences
path="gen/chat_user_ref.sql"
user_id_min = 1
user_id_max = 50
chat_id_min = 1
chat_id_max = 6
chat_users_min = 0
chat_users_max = 25

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
for chat in range(chat_id_min, chat_id_max+1):
    all_users = set(range(user_id_min, user_id_max+1))
    participants_count = random.randint(chat_users_min, chat_users_max)
    for x in range(0, participants_count):
        nextuser = random.choice(list(all_users))
        all_users.remove(nextuser)
        nextline='('+str(chat) + ', ' + str(nextuser) + ')';
        if not is_first:
            nextline = ',\n' + nextline
        else:
            is_first = False
        file.write(nextline)
file.write(';')
file.close()
