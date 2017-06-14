from ecdsa import SigningKey, NIST256p, VerifyingKey
import os


def generate_key():
    """
    Generate private key and public key
    Save as PEMs for management

    :return: ECDSA Key Object - pri_key, pub_key
    """
    key_path = os.path.dirname(os.path.dirname(__file__)) + '\NodeManager' + '\\'

    pri_key = SigningKey.generate(curve=NIST256p)
    pub_key = pri_key.get_verifying_key()

    open(key_path + "private.pem", "w").write(pri_key.to_pem())
    open(key_path + "public.pem", "w").write(pub_key.to_pem())

    return pri_key, pub_key


def get_key():
    """

    :return:
    """
    pri_key = SigningKey.from_pem(open("private.pem").read())
    pub_key = pri_key.get_verifying_key()

    return pri_key, pub_key


def key_to_string(pub_key):
    return pub_key.to_string()


def get_signature(message, private_key):
    signature = private_key.sign(message)

    return signature.encode('string_escape')


def verify_signature(public_key_str, signature, message):

    pub_key_decode = public_key_str.decode('string_escape')
    sig_decode = signature.decode('string_escape')
    public_key = VerifyingKey.from_string(pub_key_decode, curve=NIST256p)
    result = public_key.verify(sig_decode, message)

    return result
