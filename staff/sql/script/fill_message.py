import os
import random
import datetime
import string

#data for eandom generator
messages=['hello', 'bye!', 'can you help me?', 'lol', ':)', 'thx', 'get lost!!', 'of course!', 'OMG!']
sql_query = '''INSERT INTO `message`
(`user_from_id`, `chat_id`, `user_to_id`, `text`, `time`)
VALUES'''

#preferences
path="gen/message.sql"
user_id_min = 1
user_id_max = 50
chat_id_min = 1
chat_id_max = 6
chat_messages_min = 10;
chat_messages_max = 100;
user_private_messages_min = 0;
user_private_messages_max = 20;
datetime_format = '%Y-%m-%d %H:%M:%S'

#global vars
is_first = True

def genmessage(user_id, chat_id, timestamp, is_private):
    result='(' + str(user_id) + ', '
    if (is_private):
        #chat
        result+='NULL, '
        #user_to
        result+=str(random.randint(user_id_min,user_id_max))+', '
    else:
        #chat
        result+=str(chat_id)+', '
        #user_to
        result+='NULL, '
    result+='\'' +random.choice(messages) + '\', '
    reg_date = datetime.datetime.fromtimestamp(timestamp)
    result += '\'' + reg_date.strftime(datetime_format) + '\')'
    return result

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
    messages_count = random.randint(chat_messages_min, chat_messages_max)
    send_time = random.randint(1262304000, 1477958400)
    for message in range(0, messages_count):
        send_time += random.randint(30, 86400)
        user = random.randint(user_id_min, user_id_max)
        nextline=genmessage(user, chat, send_time, False)
        if not is_first:
            nextline = ',\n' + nextline
        else:
            is_first = False
        file.write(nextline)

for user in range(user_id_min, user_id_max+1):
    messages_count = random.randint(user_private_messages_min, user_private_messages_max)
    send_time = random.randint(1262304000, 1477958400)
    for message in range(0, messages_count):
        send_time += random.randint(30, 86400)
        nextline=',\n' + genmessage(user, 0, send_time, True)
        file.write(nextline)
file.write(';')
file.close()
