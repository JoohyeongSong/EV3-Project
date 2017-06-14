import json
from SEChainController import Property


def get_node_ip():
    import socket

    Property.my_ip = socket.gethostbyname(socket.gethostname())

    return Property.my_ip


def create_mynode():
    import Node

    my_ip = get_node_ip()

    new_node = Node.Node(my_ip)

    mynode_jobj = {
        'type': new_node.type,
        'ip_address': new_node.ip_address
    }

    j_str = json.dumps(mynode_jobj)

    return mynode_jobj, j_str


def send_mynode(p_ip):
    from CommunicationManager import Sender

    mynode_jobj = {
        'type': 'N',
        'ip_address': 'p_ip'
    }

    j_str = json.dumps(mynode_jobj)
    # Sender.send_to_all(j_str)
    Sender.send(p_ip, 'TEST', Property.port)
    return True

