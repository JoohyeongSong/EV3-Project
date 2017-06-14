
class Node(object):
    def __init__(self, p_ip_address):
        """
        Construct my node instance with ip address

        :param p_ip_address:
        """
        self.type = 'N'
        self.ip_address = p_ip_address
        self.public_key = ''
        self.private_key = ''