#include <iostream>
#include <random>
#include <chrono>
#include <vector>

using namespace std;

//declarations
double sortArray(int array[], int arraySize);
void createData(int array[], vector<int>& vector, int size);
double sortVector(vector<int>& vector);
string checkArraySort(int array[], int arraySize);
string checkVectorSort(vector<int>& vector);

int main() {

	//predefined array/vector size
	const int arraySize = 10000;

	vector<int> intVector;
	int array[arraySize];

	createData(array, intVector, arraySize);

	cout << "using bubble sort in micro seconds for array: " << sortArray(array, arraySize) << endl;
	cout << "sorted status: " << checkArraySort(array, arraySize) << endl;

	cout << "using bubble sort in micro seconds for vector: " << sortVector(intVector) << endl;
	cout << "sorted status: " << checkVectorSort(intVector) << endl;
	


	return 0;
}

//sorts the array using bubblesort
double sortArray(int array[], int arraySize) {

	auto start = std::chrono::steady_clock::now();

	for (int i = 0; i < arraySize - 1; i++) {
		for (int j = i+1; j < arraySize;j++) {
			if (array[i] > array[j]) {
				int temp = array[i];
				array[i] = array[j];
				array[j] = temp;
			}
		}
	}

	auto end = std::chrono::steady_clock::now();
	auto duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start);

	return duration.count();
}

//ensures the array is sorted
string checkArraySort(int array[], int arraySize) {

	bool sorted = true;

	for (int i = 0; i < arraySize - 1; i++) {
		if (array[i] > array[i + 1]) {
			sorted = false;
		}
	}

	if (sorted == false) {
		return "not sorted";
	}
	return "sorted";
}

//ensures the vector is sorted
string checkVectorSort(vector<int> &vector) {

	bool sorted = true;

	for (int i = 0; i < vector.size() - 1; i++) {
		if (vector[i] > vector[i + 1]) {
			sorted = false;
		}
	}

	if (sorted == false) {
		return "not sorted";
	}
	return "sorted";
}

//sorts the vector using bubblesort
double sortVector(vector<int>& vector) {

	auto start = std::chrono::steady_clock::now();

	for (int i = 0; i < vector.size() - 1; i++) {
		for (int j = i + 1; j < vector.size();j++) {
			if (vector[i] > vector[j]) {
				int temp = vector[i];
				vector[i] = vector[j];
				vector[j] = temp;
			}
		}
	}

	auto end = std::chrono::steady_clock::now();
	auto duration = std::chrono::duration_cast<std::chrono::microseconds>(end - start);

	return duration.count();
}


//creates vector and the array with random numbers of 1-1000
void createData(int array[], vector<int>& vector, int arraySize) {

	std::random_device rd;
	std::mt19937 gen(rd());

	std::uniform_int_distribution<> dis(1, 1000);

	for (int i = 0; i < arraySize; i++) {

		int num = dis(gen);
		array[i] = num;
		vector.push_back(num);

	}

}
