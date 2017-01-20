#!/usr/bin/python3
import MySQLdb

db = MySQLdb.connect(host="91.107.105.23",    # your host, usually localhost
                     user="blackjack",         # your username
                     passwd="Ufh4;uer46!2Weu*745UFd44<)cd5R!975",  # your password
                     db="black_jack")        # name of the data base
sql ="UPDATE user SET password = '%s' WHERE email = '%s'"

cur = db.cursor()
try:
    cur.execute(sql % ("29a50065d0258e52180c37af9936c5ca030e6020a30bf87a3da48a455b150ad1","admin@qqq175.org"))
    db.commit()
    print("Success: admin@qqq175.org")
except Exception as e:
    print("Error: admin@qqq175.org")
    print(e)

try:
    cur.execute(sql % ("1e9d631ca136f2e7a298511a61b645a1ab016154851e86815f14438fbf7703fe","user@qqq175.org"))
    db.commit()
    print("Success: user@qqq175.org")
except Exception as e:
    print("Error: user@qqq175.org")
    print(e)

db.close()
