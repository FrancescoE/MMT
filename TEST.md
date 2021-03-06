# Test infrastructure

## Contribute with a new test
All the tests are contained under the directory 'ModernMT/test/tests'.

The easiest way to create a new test is to launch the script 'create_new_test.py' and to pass as argument the name of the test that you would like to create.

In order to do that, from the root directory of the ModernMT project launch the following commands:
```bash
cd test
python create_new_test.py <TEST_NAME>
```

For example to create "My_new_test" run:
```bash
cd test
python create_new_test.py My_new_test
```

The 'create_new_test.py' script will generate a directory under the direcotry 'ModernMT/test/tests' with the same name that you specified as argument.

Under this new directory it also creates two files:

  * launch.sh
  * info.json

#### launch.sh

'launch.sh' is the script that will be launched automatically by the 'ModernMT/test/run_all_tests.py' script.

Edit 'launch.sh' and fill it with your piece of software that performs the test. You can use any programming language, although interpreted languages (e.g. python, sh, perl) are recommended; compiled languages (e.g. java) are not so encouraged because of their complexity and time requirements.

Note: use the string "$@" (double quotes included) to forward the arguments of the bash script to your software test.

The following commands show how to launch your test and forward arguments to it, in case of Python, bash and PHP programming languages respectively:
```bash
#Python:
python MyNewTest.py "$@"
#Bash:
./MyNewTest.sh "$@"
#PHP:
php MyNewTest.php "$@"
```

It is strongly recommended to use an interpreted programming language to write your test.

#### info.json

The json file named 'info.json' contains meta information about the test and it is located inside the root folder of each test.

'info.json' must contain at least the following keys:
```
{
  "enabled": true|false,
  "description": "A description of the test",
  "full_description": "A complete and exhaustive description of the test",
  "author": "Your name" 
}
```

## Input

Every test can be launched with no arguments specified and it runs with some default parameters.

Every test has to output useful information about its usage if launched with the "-h" or "--help" arguments.

## Output

Each test prints on the standard ouput a json containg both a fixed structure and a map depending on the nature of the test.

The structure of the json is the following:
```
{  
   "passed": true|false,
   "results": {  
      <USEFUL_INFORMATION_ABOUT_THE_RESULTS_OF_THE_TEST>
   }
}
```

An example of the output of a test could be the following:
```json
{  
   "passed":true,
   "results":{  
      "recall": 0.7,
      "minAllowedRecall": 0.5,
      "precision": 0.8,
      "minAllowedPrecision": 0.6,
      "avgQueryTime": 200,
      "minAllowedQueryTime": 100
   }
}
```

## Launch a test

Move into the test directory from the root directory of the ModerMT project with:

```bash
cd test
```

Now you an list all the available test with:

```bash
python run_tests.py --list
```

It outputs useful information about which tests are enabled, which are not and which have a malformed test.json:

```
## Enabled tests ## 
#Test_name Enabled Description
My_New_Test_1 True  "It tests the precision of ..."
My_New_Test_4 True  "It tests the throughput of ..."

## Disabled tests ## 
#Test_name Enabled Description
My_New_Test_3 False  "It tests the recall of ..."
My_New_Test_5 False  "It tests the alignment of ..."

## Malformed tests ## 
#Test_name Enabled Description
My_New_Test_2 malformed	"Expecting property name: line 5 column 2 (char 171)"
My_New_Test_6 malformed	"The key 'enabled' is missing"

```

To execute all the enabled tests run run_tests.py with the "-all" argument:

```bash
python run_tests.py --all
```

To execute a specific test execute run_tests.py and pass the name of the test as argument:

```bash
python run_tests.py --name My_New_Test_1
```

If you want to run a test with a custom input, you can move under its folder with:
```bash
cd tests/<TEST_NAME>/
```

And execute 'launch.sh' with '-h' argument to read about its usage:
```bash
./launch.sh -h
usage: run_tests.py [-h]
                    [-a | -n TEST_NAME | -l | -e TEST_NAME | -d TEST_NAME | -i TEST_NAME]

optional arguments:
  -h, --help            show this help message and exit
  -a, -all              Run all the enabled tests
  -n TEST_NAME, --name TEST_NAME
                        Run the specified test
  -l, --list            List all the available tests
  -e TEST_NAME, --enable TEST_NAME
                        Enable the specified test
  -d TEST_NAME, --disable TEST_NAME
                        Disable the specified test
  -i TEST_NAME, --info TEST_NAME
                        Print information about the specified test
```
