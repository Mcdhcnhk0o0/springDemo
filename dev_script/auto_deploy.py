import os
import sys
import time
import platform


# how to start:
# python3 auto_deploy.py your_password

def get_latest_timestamp(filenames):
    return max([get_timestamp_from_filename(filename)
                for filename in filenames if filename.startswith(remote_jar_prefix)])


def get_timestamp_from_filename(filename):
    # remote jar: com.example.springdemo_1700362918.jar
    splits = filename.split("_")
    if len(splits) != 2:
        return 0
    timestamp = 0
    try:
        timestamp = int(splits[1][:-4])
    except ValueError:
        print("can not get timestamp from filename, return default value: 0")
    return timestamp


class AutoDeployMonitor:

    latest_timestamp = None
    need_restart_service = False

    def __init__(self, monitor_path, jar_name):
        self.monitor_path = monitor_path
        self.jar_name = jar_name

    def _restart_service(self, password):
        self.print("prepare to restart service...")
        start_command = f"nohup java -Djasypt.encryptor.password={password} -jar com.example.springdemo_{self.latest_timestamp}.jar > {self.latest_timestamp}.out &"
        if self._java_process_exist():
            os.system("pkill java")
            self.print("java process exist, kill it")
        os.system(start_command)
        time.sleep(5)
        retry_count = 0
        while not self._java_process_exist() and retry_count < 10:
            retry_count += 1
            os.system(start_command)
            time.sleep(10)
            self.print(f"start service failed! retry count: {retry_count}")
        if retry_count == 10:
            self.print("start service failed! check your environment")
        else:
            self.print("restart service succeed!")

    @staticmethod
    def print(text):
        print(f"{time.asctime()}: " + text)

    def _java_process_exist(self):
        os.system('ps -ef|grep "springdemo" > java_status.out')
        with open(r"java_status.out") as f:
            for line in f:
                if f"{self.jar_name}" in line:
                    return True
            return False

    def start(self, password):
        self.print("AutoDeployMonitor start!")
        while True:
            if "linux" not in platform.system().lower():
                raise RuntimeError("only support running on linux")
            files = os.listdir(self.monitor_path)
            current_latest_timestamp = get_latest_timestamp(files)
            if self.latest_timestamp is None:
                self.latest_timestamp = current_latest_timestamp
                self.print(f"service start! find latest timestamp: {current_latest_timestamp}")
            else:
                if current_latest_timestamp > self.latest_timestamp:
                    self.print(f"find new latest timestamp: {current_latest_timestamp}")
                    self.need_restart_service = True
                    self.latest_timestamp = current_latest_timestamp
            if self.need_restart_service:
                self._restart_service(password)
                self.need_restart_service = False
            time.sleep(10)


if __name__ == "__main__":
    remote_jar_dir = "/root/home/packages/springdemo"
    remote_jar_prefix = "com.example.springdemo"
    jasypt_password = ""
    if len(sys.argv) > 1:
        jasypt_password = sys.argv[1]
    monitor = AutoDeployMonitor(remote_jar_dir, remote_jar_prefix)
    monitor.start(jasypt_password)
