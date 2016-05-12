import subprocess

def run_command(command):
    p = subprocess.Popen(command,
                     stdout=subprocess.PIPE,
                     stderr=subprocess.STDOUT)
    return iter(p.stdout.readline, b'')


number_of_clients = 40
basic_args = ['java', '-jar', '-Dhost=192.168.0.19', 'target/netty-example-client-jar-with-dependencies.jar']

for num in range(number_of_clients):
    ext_args = list(basic_args)
    ext_args.append(str(num))
    print(ext_args)
    #subprocess.call(ext_args)
    run_command(ext_args)