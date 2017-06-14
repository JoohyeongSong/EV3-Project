import os
import json

database_path = os.path.dirname(os.path.dirname(__file__)) + '\_DataStorage' + '\\'
block_storage_path = os.path.dirname(os.path.dirname(__file__)) + '\_BlockStorage' + '\\'
key_path = os.path.dirname(os.path.dirname(__file__)) + '\NodeManager' + '\\'
node_info_file = 'NodeInfo.txt'
ledger_file = 'Transactions.txt'
vote_file = 'VoteResult.txt'

# ================================================
# BASIC FUNCTIONS
# ================================================


def write(p_fname, p_msg):
    """
    Basic write function
    Write p_msg in the file name "p_fname"

    :param p_fname:
    :param p_msg:
    :return: None
    """
    f = open(p_fname, 'a')
    f.write(p_msg)
    f.write('\n')
    f.close()


def read_all_line(p_fname):
    """
    Read all data in the file "p_fname"
    Return read data as list

    :param p_fname:
    :return: All line of file
    """
    f = open(p_fname, 'r')
    line_list = []

    while True:
        line = f.readline()
        if not line:
            break
        else:
            line_list.append(line)

    f.close()
    return line_list

# ================================================
# TRANSACTION
# ================================================


def add_transaction(p_tx):
    """
    Adding the transaction into local transaction file, "Transactions.txt"

    :param p_tx: received transaction from network.
    :return: None
    """
    write(database_path + ledger_file, p_tx)


def get_transaction_list():
    """
    Read all transactions in the ledger file, "Transactions.txt"

    :return: tx_list
    """
    tx_list = read_all_line(database_path + ledger_file)
    return tx_list


def get_transaction_count():
    """
    :return: length of transaction list, i.e. number of transaction
    """
    return len(get_transaction_list())


def remove_all_transaction():
    """
    Delete all transaction, modify the ledger to blank file.

    :return: None
    """
    f = open(database_path + ledger_file, 'w')
    f.write("")
    f.close()

# ================================================
# NODE
# ================================================


def get_node_list():
    """
    Collect node list

    :return: each node's information as list
    """
    try:
        f = open(database_path + node_info_file, 'r')
        node_list = []

    except IOError as e:
        return None

    while True:
        line = f.readline()
        if not line:
            break
        if line == "":
            break
        node_list.append(line)

    return node_list


def get_ip_list():
    """
    Get ip address of connected nodes

    :return: ip address' as list
    """
    import json

    f = open(database_path + node_info_file, 'r')
    ip_list =[]

    while True:
        line = f.readline()

        if not line:
            break
        if line == "":
            break

        node_info = json.loads(line)
        ip_list.append(node_info['ip_address'])

    return ip_list


def add_node(p_node):
    """
    Adding new node from network into "NodeInfo.txt"
    Before adding node, check if recevied node is in the list

    :param p_node: received node information from other node -- JSON string
    :return: None
    """
    node_list = get_node_list()
    check_flag = False

    if node_list is None:
        write(database_path + node_info_file, p_node)

    else:
        recv_node_jobj = json.loads(p_node)
        recv_ip = str(recv_node_jobj['ip_address'])

        for outer_list in node_list:
            outer_item = str(outer_list)

            if recv_ip in outer_item:
                #print "Already Connected Node"
                check_flag = True

        if check_flag is False:
            write(database_path + node_info_file, p_node)
            print "New node is recorded"

    return True


# ================================================
# BLOCK
# ================================================


def add_block(p_fname, p_block_json):
    """
    Create new block file into "/_BlockStorage/~"

    :param p_fname: new block file name,
    :param p_block_json: json string, received or created block
    :return: None
    """
    f = open(block_storage_path + p_fname, 'w')
    f.write(p_block_json)
    f.close()


def get_last_block():
    """
    Get last block file in local storage

    :return: last_block_name -- block file name (B201702~)
    :return: last_block -- JSON format string
    """

    block_list = []
    for (path, dir, files) in os.walk(block_storage_path):
        block_list = files

    last_block_name = block_list[-1]
    last_block_data = read_all_line(block_storage_path + last_block_name)
    last_block = "\n".join(last_block_data)

    return last_block_name, last_block


def get_blocklist():
    """
    Get all blocks in local storage

    :return: block list -- list
    """
    block_list = []

    for(path, dir, files) in os.walk(block_storage_path):
        for iter in files:
            temp_block = read_all_line(block_storage_path + iter)
            blocks = "\n".join(temp_block)
            block_list.append(blocks)

    return block_list


def get_block_height():
    """
    Get local block height

    :return: length of block file -- int
    """
    return len(os.walk(block_storage_path).next()[2])


