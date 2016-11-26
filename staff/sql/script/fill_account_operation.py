import os
import random
import datetime
import string

#data for eandom generator
operation_types = ['payment', 'withdrawal', 'win', 'loss', 'buy']
sql_query = '''INSERT INTO `account_operation`
(`user_id`, `ammount`, `type`, `time`, `comment`)
VALUES'''

#preferences
path="gen/account_operation.sql"
user_id_min = 1
user_id_max = 50
operation_count = 500
datetime_format = '%Y-%m-%d %H:%M:%S'

#global vars
is_first = True

def genoper(user_id, operation_type, max_value):
    result='(' + str(user_id) + ', '
    result+=str(random.randint(0,max_value-1)) + '.' + str(random.randint(0,99)) + ', '
    result+= '\'' + operation_type + '\', '
    reg_date = datetime.datetime.fromtimestamp(random.randint(1300000000, 1477958400))
    result += '\'' + reg_date.strftime(datetime_format) + '\', '
    if random.randint(0,9) < 7:
        result=result + 'NULL)'
    else:
        result=result + '\'some comment ' +str(random.randint(1,10)) + '\')'
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
    nextline=genoper(user, operation_types[0], 10)
    if not is_first:
        nextline = ',\n' + nextline
    else:
        is_first = False
    file.write(nextline)
for x in range(0, operation_count):
    user=random.randint(user_id_min, user_id_max)
    nextline= ',\n' + genoper(user, random.choice(operation_types), 4)
    file.write(nextline)
file.write(';')
file.close()
