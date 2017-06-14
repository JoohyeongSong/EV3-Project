import time
import hashlib


class Block(object):

    def __init__(self, p_prev_block_hash, p_tx_list, p_merkle_root, p_nonce):
        """
        Construct new block

        :param p_prev_block_hash: It contains hashed information about previous block -- string
        :param p_tx_list: It contains currently confirmed transactions -- list
        :param p_merkle_root: Hash tree, leaf is individual transaction -- string
        :param p_nonce: Mining puzzle -- int

        """
        self.version = 0.001
        self.type = 'B'
        self.prev_block_hash = p_prev_block_hash
        self.tx_list = p_tx_list
        self.merkle_root = p_merkle_root
        self.nonce = p_nonce
        self.timestamp = time.strftime('%Y%m%d%H%M%S', time.localtime())
        self.block_id = "B" + self.timestamp
        self.block_hash = ''


class GenesisBlock():
    def __init__(self):
        self.type = 'B'
        self.prev_block_hash = hashlib.sha256("gen_block").hexdigest()
        self.timestamp = '0000-00-00-00-00-00'
        self.block_id = 'B000000000000'
        self.block_hash = hashlib.sha256('SogangSELAB').hexdigest()
        self.nonce = 20170101
        self.merkle_root = '937ba9042411aae82e555f494619e745a061d3b095ef407367156b5ea6ab69cf'


if __name__ == '__main__':
    import json
    t = GenesisBlock()
    temp = json.dumps(t, indent=4, default=lambda o: o.__dict__, sort_keys=True)
    temps = json.loads(temp)

    print temps