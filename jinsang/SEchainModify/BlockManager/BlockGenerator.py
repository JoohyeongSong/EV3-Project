from FileManager import FileController
from CommunicationManager import Sender
from ConsensusManager import MerkleTree
from ConsensusManager import PoW
import json
import hashlib
import Block


class BlockGenerator(object):
    def __init__(self):
        pass

    @staticmethod
    def generate_block():

        last_block_id, last_block = FileController.get_last_block()
        last_block_jobj = json.loads(last_block)
        prev_block_hash = last_block_jobj['block_hash']

        transactions = FileController.get_transaction_list()

        merkle_cls = MerkleTree.MerkleTree()
        merkle_root = merkle_cls.get_merkle(transactions)

        # p_nonce
        current_nonce = PoW.proof_of_work(transactions)

        # create block as json string
        block = Block.Block(prev_block_hash, transactions, merkle_root, p_nonce=00001)
        block.nonce = current_nonce

        block_jstr = json.dumps(block, indent=4, default=lambda o: o.__dict__, sort_keys=True)

        # block hash
        current_block_hash = hashlib.sha256(block_jstr).hexdigest()
        block.block_hash = current_block_hash

        block_jstr = json.dumps(block, indent=4, default=lambda o: o.__dict__, sort_keys=True)

        # save at local storage
        FileController.add_block(block.block_id, block_jstr)

        # send to all nodes

        #print "here?5"

        #Sender.send_to_all(block_jstr)
        #print "here?6"

    def genisis_block(self):
        block_id = 'B000000000000'
        block = Block.GenesisBlock()
        genblock_json_str = json.dumps(block, indent=4, default=lambda o: o.__dict__, sort_keys=True)

        print "here?3"
        print genblock_json_str
        print "here?4"
        FileController.add_block(block_id, genblock_json_str)

# MODULE TEST
if __name__ == '__main__':

    BlockG = BlockGenerator()
    BlockG.genisis_block()