#include <iostream>
#include <string>
#include <chrono>

//does factorial calculation using a for-loop
size_t calculateFactorial(size_t num) {

    size_t total = 1;

    for (int i = 1; i < num + 1; i++) {
        total *= i;
    }

    return total;
}

//logic for combination which is timed and printed.
void doCombination(size_t inputn, size_t inputr) {

    //n must be greater than r in order to avoid negative cases
    if (inputn < inputr) {
        std::cout << "Error in combination: n must be greater or equal to r" << std::endl;
        return;
    }

    auto start = std::chrono::high_resolution_clock::now(); //start the timer

    //combination formula
    size_t result = calculateFactorial(inputn) / (calculateFactorial(inputr) * (calculateFactorial(inputn - inputr)));

    auto end = std::chrono::high_resolution_clock::now(); //end timer

    // Calculate the duration (execution time) and print
    std::string duration = std::to_string((std::chrono::duration_cast<std::chrono::nanoseconds>(end - start).count()));
    std::cout << "The combination of " << inputn << " and " << inputr << " is " << result << ". Done in " << duration << "ns" << std::endl;
}

//logic for factorial which is timed and printed.
void doFactorial(size_t input) {

    auto start = std::chrono::high_resolution_clock::now();   //start the timer

    size_t factorial = calculateFactorial(input);

    auto end = std::chrono::high_resolution_clock::now(); // Stop the timer

    // Calculate the duration (execution time) and print
    std::string duration = std::to_string((std::chrono::duration_cast<std::chrono::nanoseconds>(end - start).count()));
    std::cout << "The factorial of " << input << " is " << factorial << ". Done in " << duration << "ns" << std::endl;

}

// Returns a valid positive input from the user
size_t getNum() {
    long input;
    bool validInput = false;

    do {
        // Ask for the input
        std::cin >> input;

        // Check if the input is valid
        if (std::cin.fail() || input < 0) {
            std::cout << "Invalid input. Please enter a positive number that is not too big.\n";
            std::cin.clear(); // Clear the error flag
            std::cin.ignore(10000, '\n'); // Discard invalid input
        }
        else {
            validInput = true;
        }
    } while (!validInput);

    return input;
}

int main() {

    while (true) {

        //gets initial user request
        std::cout << "Enter the 1 for normal factorial or 2 for combination 3 to do both or any other number to close: " << std::endl;;
        size_t input = getNum();

        //input options
        if (input == 1) {

            std::cout << "Enter a positive number : " << std::endl;;
            size_t input = getNum();
            doFactorial(input);
        }

        else if (input == 2 || input == 3) {

            std::cout << "Enter a positive number for n : " << std::endl;;
            size_t inputn = getNum();
            std::cout << "Enter a positive number for r : " << std::endl;;
            size_t inputr = getNum();

            doCombination(inputn, inputr);

            //if combination and factorial are both requested
            if (input == 3) {
                doFactorial(inputn);
            }
        }

        else {
            //close
            return 0;
        }
    }
}