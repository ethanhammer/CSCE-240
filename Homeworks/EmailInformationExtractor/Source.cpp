
#include <iostream>
#include <fstream>
#include <string>
#include <regex>

void lookForSection(std::ifstream& file) {

    //gets and stores desired part
    std::cout << "What section are you looking for?" << std::endl;
    std::string section;
    std::cin >> section;

    //paterns for when to start and end search
    std::regex patternEnd("\\b\\w+:\\s");
    std::regex patternBegin("\\b" + section + ":\\s");

    //current file line
    std::string line;
    
    //looks for beginning pattern and pritns until it hits end pattern
    while (std::getline(file, line)) {
        if (std::regex_search(line, patternBegin)) {
            do {
                std::cout << line << std::endl;
                std::getline(file, line);
            } while (!std::regex_search(line, patternEnd));
        }
    }
}

int main() {

    //infinite loop
    while(true) {

        //ccurrent file
        std::ifstream file;
        //current option
        int num1;
        //ensures input is a number
        bool validInput = false;

        do {
            // Ask for the first number
            std::cout << "Enter the 1 for gmail or 2 for outlook or any other number to close: " << std::endl;;
            std::cin >> num1;

            // Check if the input is valid
            if (std::cin.fail()) {
                std::cout << "Invalid input. Please enter a valid number.\n";
                std::cin.clear(); // Clear the error flag
                std::cin.ignore(10000, '\n'); // Discard invalid input
            }
            else {
                validInput = true;
            }
        } while (!validInput);

       //decides file based on input
        if (num1 == 1) {
            file.open("gmail.txt");
         }
        else  if (num1 ==2) {
            file.open("email.txt");
        }
        else {
             //closes program
             return 0;
        }

         //makes sure file is open
        if (!file.is_open()) {
            std::cerr << "Error opening file\n";
            return 1;
        }

        //starts section search
        lookForSection(file);

        //closes file
        file.close();
    }
    return 0;
}

