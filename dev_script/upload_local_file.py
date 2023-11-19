import os
import sys
import time


remote_server = "root@123.249.16.84"
remote_path = "/root/home/packages/springdemo"

local_file_name = "com.example.springdemo"


def upload_local_jar():
    current_time = int(time.time())
    new_name = f"{local_file_name}_{current_time}.jar"
    os.rename(f"../target/{local_file_name}.jar", new_name)
    upload_local_file(f"./{new_name}")


def upload_local_file(file_path):
    os.system(f"scp {file_path} {remote_server}:{remote_path}")


if __name__ == "__main__":
    if len(sys.argv) > 1:
        print(f"upload file {sys.argv[1]} ...")
        upload_local_file(sys.argv[1])
    else:
        print("upload build jar by default...")
        upload_local_jar()
    print("upload complete!")
