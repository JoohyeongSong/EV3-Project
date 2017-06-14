import Property
from CommunicationManager import Receiver
from socket import *
from NodeManager import KeyController
from FileManager import FileController
from SEChainController import Property
from BlockManager import BlockThread
from BlockManager import BlockGenerator
from flask import Flask, request
import threading
import json



app = Flask(__name__)


@app.route('/')
def hello_world():



    return 'Hello World!'


@app.route('/path', methods=['POST', 'GET'])
def path():
    #error = None
    #return 'ssibal'
    #path_info = str(request.form['test'])
    if request.method == 'POST':
        #print request.form['test']

        path_info = str(request.form['test'])


        recv_thread = Receiver.ReceiverThread(1, "RECEIVER", Property.my_ip, Property.port)
        recv_thread.start()

        MainController.command_line_interface(path_info)

        print "1"

        #MainController.set_node()
        #recv_thread = Receiver.ReceiverThread(1, "RECEIVER", Property.my_ip, Property.port)
        #recv_thread.start()

        #MainController.command_line_interface()




        #trx_jstr = TransactionCotroller.create_transaction(Property.pub_key, Property.pri_key, receiver, data)


        # here!

        #receiver = raw_input('Receiver IP: ')

        #trx_jstr = TransactionCotroller.create_transaction(Property.pub_key, Property.pri_key,receiver, "test")
        #MainController.set_node()
        #MainController.command_line_interface(request.form['path]'])
        #receiver = raw_input('Receiver IP: ')
        #block_generator = BlockGenerator.BlockGenerator
        #block_generator.generate_block()

        return request.form['test']
        #return








class MainController(object):
    def __init__(self):
        return 0

    @staticmethod
    def set_node():
        """
        Set up my node and record to "NodeInfo.txt"

        :return: True
        """
        from NodeManager import KeyController
        from NodeManager import NodeController
        from FileManager import FileController

        key_path = FileController.key_path + "private.pem"

        try:
            f = open(key_path)
            f.close()
            pri_key, pub_key = KeyController.get_key()

        except IOError as e:
            pri_key, pub_key = KeyController.generate_key()

        Property.my_node_jobj, Property.my_node_jstr = NodeController.create_mynode()


        Property.pri_key = pri_key
        Property.pub_key = pub_key
        Property.my_ip = Property.my_node_jobj['ip_address']

        FileController.add_node(Property.my_node_jstr)

        # NodeController.send_mynode('192.168.56.1')

        return True

    @staticmethod
    def command_line_interface(info):
        from TransactionManager import TransactionCotroller
        from CommunicationManager import Sender
        import Property
        from NodeManager import KeyController
        from FileManager import FileController

        cmd = None

        while cmd != 'q':
            cmd = raw_input('[t: Make Transaction?, q: quit] > ')

            if cmd == 't':
                receiver = "1.2.3.4"
                #receiver = raw_input('Receiver IP: ')
                #message = raw_input('Message: ')
                message = info

                print "Transaction : "

                #print "here?!!!!!!!!"

                key_path = "C:\Users\R912\Desktop\SEchainModify\NodeManager\private.pem"

                try:
                    f = open(key_path)
                    f.close()
                    pri_key, pub_key = KeyController.get_key()

                except IOError as k:
                    pri_key, pub_key = KeyController.generate_key()


                Property.pri_key = pri_key
                Property.pub_key = pub_key


                recv_data = TransactionCotroller.create_transaction(Property.pub_key, Property.pri_key, receiver, message)
                #print "here?10"
                print recv_data
                #print "here?11"
                json.loads(recv_data)
                #print "here?12"
                #print "Transaction received"
                #FileController.add_transaction(recv_data)
                #print "here?13"

                block_generator = BlockGenerator.BlockGenerator
                #print "here?14"
                block_generator.generate_block()
                #print "here?15"


                #Sender.send_to_all(trx_jstr)
                #print "here?2"
                Property.tx_count += 1


if __name__ == '__main__':
    #app.run(debug=True, host='0.0.0.0')



    MainController.set_node()
    print Property.my_ip

    app.run(debug=True, host='0.0.0.0')

    recv_thread = Receiver.ReceiverThread(1, "RECEIVER", Property.my_ip, Property.port)
    recv_thread.start()

    MainController.command_line_interface()