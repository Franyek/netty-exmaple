import subprocess

number_of_clients = 1
basic_args = ['java', '-jar', 'Blender.jar']

for num in range(number_of_clients):
    subprocess.call(basic_args, num)