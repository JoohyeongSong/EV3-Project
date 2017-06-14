from socket import *
from NodeManager import KeyController
from FileManager import FileController
from SEChainController import Property
from BlockManager import BlockThread
from BlockManager import BlockGenerator
import threading
import json


class ReceiverThread(threading.Thread):
    def __init__(self, p_thrd_id, p_thrd_name, p_ip, p_port):
        """

        :param p_thrd_id:
        :param p_thrd_name:
        :param p_ip:
        :param p_port:
        """
        threading.Thread.__init__(self)
        self.thrd_name = p_thrd_name
        self.thrd_id = p_thrd_id
        self.thrd_ip = p_ip
        self.thrd_port = p_port

    def run(self):
        #print "Start Receiver Thread"
        receiver(self.thrd_name, self.thrd_ip, self.thrd_port)


def receiver(p_thrd_name, p_ip, p_port):
    """

    :param p_thrd_name:
    :param p_ip:
    :param p_port:
    :return:
    """
    addr = (p_ip, p_port)
    buf_size = 10000

    tcp_socket = socket(AF_INET, SOCK_STREAM)
    tcp_socket.bind(addr)
    tcp_socket.listen(5)

    while True:
        request_sock, request_ip = tcp_socket.accept()

        while True:
            recv_data = request_sock.recv(buf_size)

            try:
                if recv_data == "":
                    break

                data_jobj = json.loads(recv_data)

                if data_jobj['type'] is 'T':
                    print "Transaction received"
                    verify_msg = data_jobj['message']

                    verification = KeyController.verify_signature(data_jobj['pub_key'], data_jobj['signature'],
                                                                  verify_msg)
                    if verification is True:
                        FileController.add_transaction(recv_data)
                        Property.tx_count += 1

                    if Property.tx_count == 10:
                        print "start consensus protocol"
                        # For single node test...
                        block_generator = BlockGenerator.BlockGenerator
                        block_generator.generate_block()

                elif data_jobj['type'] is 'B':
                    print "Block received"

                    # block verification thread
                    block_thrd = BlockThread.BlockThread(2, "BLOCK", data_jobj)
                    block_thrd.start()

                    # remove all txs
                    FileController.remove_all_transaction()

                elif data_jobj['type'] is 'N':
                    print "new node connected"

                    node_list = FileController.get_ip_list()
                    received_ip = data_jobj['ip_address']

                    sync_flag = False

                    for outer_list in node_list:
                        outer_list = str(outer_list)
                        if outer_list == received_ip:
                            sync_flag = True

                    if sync_flag is False:
                        FileController.add_node(recv_data)

            except Exception as e:
                print "SOCKET ERROR", e

            finally:
                tcp_socket.close()

            break
