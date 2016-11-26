import os
import random
import datetime
import string

#data for eandom generator
game_types = ['computer', 'player']
sql_query = '''INSERT INTO `userstat`
(user_id, game_type, wins, losses, ties)
VALUES'''

#preferences
path="gen/userstat.sql"
user_id_min = 1
user_id_max = 50

#global vars
is_first = True

def genstat(user_id, game_type):
    result='(' + str(user_id) + ', \''+ game_type + '\', '
    result+=str(random.randint(0,20))+', '
    result+=str(random.randint(0,20))+', '
    result+=str(random.randint(0,5))+')'
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
for user in range(user_id_min, user_id_max+1):
    for type in game_types:
        nextline=genstat(user, type)
        if not is_first:
            nextline = ',\n' + nextline
        else:
            is_first = False
        file.write(nextline)
file.write(';')
file.close()
