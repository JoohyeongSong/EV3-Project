import threading
from FileManager import FileController
from ConsensusManager import MerkleTree
import hashlib


class BlockThread(threading.Thread):
    def __init__(self, p_thrd_id, p_thrd_name, p_block):
        """

        :param p_thrd_id:
        :param p_thrd_name:
        :param p_block:
        """
        threading.Thread.__init__(self)
        self.thrd_id = p_thrd_id
        self.thrd_name = p_thrd_name
        self.block_jobj = p_block

    def run(self):
        print "a"


# 1) check in-block transactions and local transaction => compare merkle root
    # 2) check block.hash
    # if 1) && 2) is True, store current block in local block storage.
def BlockVerifier(p_block_jobj):
    """

    :param p_block_jobj:
    :return:
    """
    mk_root_flag, block_hash_flag = False, False

    m_tx_list = FileController.get_transaction_list()

    merkle_cls = MerkleTree.MerkleTree()
    merkle_root = merkle_cls.get_merkle(m_tx_list)

    if p_block_jobj['merkle_root'] == merkle_root:
        mk_root_flag = True

    block_info = p_block_jobj['version'] + p_block_jobj['type'] + p_block_jobj['prev_block_hash'] + \
                 p_block_jobj['merkle_root'] + p_block_jobj['nonce'] + p_block_jobj['timestamp'] + \
                 p_block_jobj['block_id']

    block_info_hash = hashlib.sha256(block_info).hexdigest()

    if block_info_hash == p_block_jobj['block_hash']:
        block_hash_flag = True

    if mk_root_flag is True and block_hash_flag is True:
        print "Block saved"
        FileController.add_block(p_block_jobj['block_id'], p_block_jobj)