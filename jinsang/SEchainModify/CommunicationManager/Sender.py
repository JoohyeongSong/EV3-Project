from socket import *
from FileManager import FileController
from SEChainController import Property


def send(p_ip, p_msg, p_port, *args):
    """
    Basic send function.
    Using TCP socket connection, send p_msg to (p_ip, p_port)

    :param p_ip: receiver's ip address
    :param p_msg: can be transaction, block, node information
    :param p_port: pre-defined port
    :param args: None

    :return: None
    """

    print p_ip
    print type(p_ip)
    p_ip = "1.1.1.1"
    print p_ip
    #print Property.my_ip


    if p_ip == Property.my_ip:
        print "Error"

    else:
        receiver_addr = (p_ip, p_port)
        tcp_socket = socket(AF_INET, SOCK_STREAM)

        try:
            tcp_socket.connect(receiver_addr)
            tcp_socket.settimeout(2)
            tcp_socket.send(p_msg)

        except socket.error as e:
            print "SEND FAIL"

        tcp_socket.close()


def send_to_all(p_msg):
    """
    Send p_msg to all connected nodes
    implemented using send() function.

    :param p_msg: can be transaction, block, node information
    :return: None
    """
    address_list = FileController.get_ip_list()

    for iter in address_list:
        try:
            send(iter, p_msg, Property.port)
            print "here?1"

        except Exception as e:
            print "SEND FAIL2"
