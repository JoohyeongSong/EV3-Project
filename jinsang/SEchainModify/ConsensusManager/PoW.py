import hashlib
import time
from struct import unpack, pack


def proof_of_work(p_tx):
    """


    :param p_tx:
    :return: guess
    """
    timestamp = str(time.time())
    message = "This is a random message"

    for tx_item in p_tx:
        message += tx_item

    count = 0
    guess = 999999999999999

    # difficulty
    throttle = 100000
    target = 2**64 / throttle

    payload = timestamp + message
    payload_hash = hashlib.sha256(payload).digest()

    while guess > target:
        count += 1
        guess, = unpack('>Q', hashlib.sha256(hashlib.sha256(pack('>Q', count) + payload_hash).digest()).digest()[0:8])

    return guess

# =====MODULE TEST ======
if __name__ == '__main__':
    proof_of_work()