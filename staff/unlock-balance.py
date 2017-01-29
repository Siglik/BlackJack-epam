#!/usr/bin/python3
import MySQLdb

db = MySQLdb.connect(host="91.107.105.23",    # your host, usually localhost
                     user="blackjack",         # your username
                     passwd="Ufh4;uer46!2Weu*745UFd44<)cd5R!975",  # your password
                     db="black_jack")        # name of the data base
sql ="UPDATE user SET account_balance = account_balance + locked_balance, locked_balance = 0 WHERE locked_balance > 0"

cur = db.cursor()
try:
    cur.execute(sql)
    db.commit()
    print("Success: affected " +str(cur.rowcount) + " rows")
except Exception as e:
    print("Error.")
    print(e)

db.close()
