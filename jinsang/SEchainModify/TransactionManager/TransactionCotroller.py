import json
import Transaction
from NodeManager import KeyController
from FileManager import FileController


def create_transaction(p_pubkey, p_prikey, p_recv_addr, p_msg):
    """

    :param p_pubkey:
    :param p_prikey:
    :param p_recv_addr:
    :param p_msg:
    :return:
    """

    #print "hi!"

    tx = Transaction.Transaction(p_recv_addr, p_extra=p_msg)
    #print p_pubkey
    #print "yo"
    pubkey_str = KeyController.key_to_string(p_pubkey)
    #print "ya"
    tx.pub_key = pubkey_str.encode('string_escape')

    tx.message = tx.timestamp + p_msg


    tx.signature = KeyController.get_signature(tx.message, p_prikey)
    tx_jstr = json.dumps(tx, default=lambda o: o.__dict__)

    FileController.add_transaction(tx_jstr)

    return tx_jstr


# =======MODULE TEST==========
if __name__ == '__main__':
    from SEChainController import Property

    from FileManager import FileController

    key_path = FileController.key_path + "private.pem"

    try:
        f = open(key_path)
        f.close()
        Property.pri_key, Property.pub_key = KeyController.get_key()

    except IOError as e:
        Property.pri_key, Property.pub_key = KeyController.generate_key()

    # json str
    tx = create_transaction(Property.pub_key, Property.pri_key, Property.my_ip, "test")

    tx_jobj = json.loads(tx)

    # verification
    verify_msg = tx_jobj['message']

    verification = KeyController.verify_signature(tx_jobj['pub_key'], tx_jobj['signature'],
                                                  verify_msg)

    print verification
    print tx_jobj