import json
import threading

# class for reading the config files
class Config(object):
    _instance_lock = threading.Lock()
    
    config = None
    
    
    def __init__(self):
        pass
    
    def __new__(cls, *args, **kwargs):
        if not hasattr(Config, "_instance"):
            with Config._instance_lock:
                if not hasattr(Config, "_instance"):
                    Config._instance = object.__new__(cls)
        return Config._instance
    
    # function for reading config files
    def readConfig(self):
        config_file = open('/boot/conf.json', 'r')
        self.config = json.load(config_file)
        print(self.config)
        return self.config