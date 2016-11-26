import os
import random
import datetime
import string

#data for eandom generator
logins = ['filolog', 'maxim', 'kaka22', '7078009', 'fabric_two', 'jenson', 'kartoon', 'sunshine', 'rider', 'dirty_one', 'vovka', 'alex', 'ustas']
hosts = ['gmail.com', 'tut.by', 'mail.ru', 'yandex.ru', 'hotmail.com', 'yandex.by', '123.org', 'rambler.ru', 'unmail.ru']
first_name = [['Sergej', 'Anton', 'Maksim', 'Vasiliy', 'Peter', 'Joe', 'Tim'], ['Anna', 'Elena', 'Jessica', 'Sofia']]
last_name = ['Petrenko', 'Buhaj', 'Jonson', 'Fidd', 'Crane', 'Parker', 'Pupkins', 'Hokking', 'Petrosyan', 'Ananasko']
middle_name = [['Ivanovich', 'Petrovich', 'Maksimovich'], ['Ivanovna', 'Petrovna', 'Maksimovna', 'Antonovna']]
statuses = ['offline', 'away', 'online']
sql_query = '''INSERT INTO `user`
(email, password, first_name, middle_name, last_name, display_name, rating, account_balance, bonus_points_balance, type, status, registred)
VALUES'''

#preferences
count=50
admin_count=3
path="gen/user.sql"
datetime_format = '%Y-%m-%d %H:%M:%S'
pass_chars = string.digits + string.ascii_letters
pass_min_size = 8
pass_max_size = 12

#global vars
admins=0
used_emails = set()

def genpass():
    return ''.join(random.choice(pass_chars) for _ in range(random.randint(pass_min_size, pass_max_size)))

def gen_unique_email():
    global used_emails
    result = random.choice(logins) + '@' + random.choice(hosts)
    while result in used_emails:
        result = random.choice(logins) + '@' + random.choice(hosts)
    used_emails.add(result)
    return result


def genuser():
    global admins
    result='('
    #email
    result=result + '\'' + gen_unique_email() + '\', '
    #password
    result=result + 'SHA2(\'' + genpass() + '\', 256), '
    #first_name
    gender = random.randint(0,1)
    first_name_cur = random.choice(first_name[gender])
    result=result + '\'' + first_name_cur + '\', '
    #middle_name
    if random.randint(0,9) < 7:
        result=result + 'NULL, '
    else:
        result=result + '\'' + random.choice(middle_name[gender]) + '\', '
    #last_name
    last_name_cur = random.choice(last_name)
    result=result + '\'' + last_name_cur + '\', '
    #display_name
    result=result + '\'' + first_name_cur + ' ' + last_name_cur + '\', '
    #rating
    result=result + str(random.randint(-150,500)) + ', '
    #account_balance
    if random.randint(0,9) < 2:
        result=result + '0, '
    else:
        result=result + str(random.randint(0,100)) + '.' + str(random.randint(0,99)) + ', '
    #bonus_points_balance
    if random.randint(0,9) < 2:
        result=result + '0, '
    else:
        result=result + str(random.randint(0,100)) + '.' + str(random.randint(0,99)) + ', '
    #type
    type='player'
    if random.randint(0,9) > 8:
        if admins < admin_count:
            type='admin'
            admins+=1
    result=result + '\'' + type + '\', '
    #status
    result=result + '\'' + random.choice(statuses) + '\', '
    #registred
    #generate date from 01.01.2010 to 01.11.2016
    reg_date = datetime.datetime.fromtimestamp(random.randint(1262304000, 1477958400))
    registered = '\'' + reg_date.strftime(datetime_format) + '\''
    if random.randint(0,9) > 8:
        registered = 'NULL'
    result=result + registered + ')'
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
for x in range(0,count):
    nextline = genuser()
    if x != count -1:
        nextline += ',\n'
    else:
        nextline += ';'
    file.write(nextline)
file.close()
