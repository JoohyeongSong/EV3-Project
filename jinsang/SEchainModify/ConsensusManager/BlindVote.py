from FileManager import FileController
import MerkleTree


def node_decision():
    tx_list = FileController.get_transaction_list()
    merkle_cls = MerkleTree.MerkleTree()
    merkle_root = merkle_cls.get_merkle(tx_list)

    delegated_node = (long(merkle_root, 16) % 5) + 1

    return delegated_node


