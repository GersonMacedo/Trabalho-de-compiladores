scriptPath=testScripts/scripts/
outputPath=testScripts/outputs/
expectedPath=testScripts/expectedOutputs/

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

rm -r build
mkdir build
echo "Compiling the program"
javac -d ./build compilador/Main.java
if [ $? -ne 0 ]; then
    echo -e "\n${RED}Compilation failed${NC}"
    exit 1
fi

testScripts=$(ls $scriptPath)
passed=0
failed=0

rm -r $outputPath
mkdir $outputPath
echo -e "Running all the test scripts\n"
for script in $testScripts; do
    echo -e "${GREEN}Running:${NC} $script"
    args=$(head -n 1 $scriptPath$script)
    java -cp ./build compilador/Main $scriptPath$script ${args:2} >$outputPath$script
    diff $outputPath$script $expectedPath$script &>/dev/null
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}OK${NC}\n"
        ((passed++))
    else
        echo -e "${RED}Test failed${NC}\n"
        ((failed++))
    fi
done

if [ $passed -ne 0 ]; then
    echo -e "${GREEN}$passed passed tests${NC}"
fi
if [ $failed -ne 0 ]; then
    echo -e "${RED}$failed failed tests${NC}"
fi