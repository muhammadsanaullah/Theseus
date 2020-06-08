# Theseus: an A.I. Program that Solves the NY Times Daily Mini-Crossword
This project is based on the use and implementation of concepts and ideas in Artificial Intelligence and Computer Science to serve ease
in certain challenging tasks that require problem solving and analytical skills. The program created is based on solving a crossword 
puzzle; the New York Times mini crossword puzzle to be exact. It needs to be compatible, adaptable and accurate in solving all the 
daily puzzles of the week. It is divided in different AI and CS components that help in downloading the puzzle from the NY Times website
onto its own interface and then using information stored in its libraries from the internet to act as potential answers to the hints in
the puzzle. Its interface is compatible to the one on the website such that it gives the impression that this “artificially created 
intelligence” itself opens the website to solve the puzzle like any other person.

Before downloading the puzzle, the interface and library is created for the program. The Answer Package makes use of DataMuzeSolver
which acts as a search engine to find the best suited words or phrases to the hints given. The Downloader Package uses the HTMLDownloader
and SeleniumDownloader to use HTML code of the NY Times webpage in “downloading” or basically copying hints, answers, text etc. for 
the interface. The Parser Package uses the HTMLParser and JSONParse to make suitable the downloaded information to be used in user
interface and data matrices. The Puzzle and UI Packages create the platform for the user to interact with the program and see the magic
happen. Once the program’s interface is created and the puzzle downloaded, the CS part of the program is completed. It gets more tricky 
in the AI part, which is basically solving the puzzle.

For the program to be able to solve the puzzle “intelligently”, it needed to filter out unnecessary information. An exhaustive search 
would have taken too long to try to match words in rows or columns. Therefore specific answers and entries were taken from the databases
with the most relevance and probability. DataMuse’s and Google API’s weights were used to find which answer is most probable. Then using
most probable entries to best fit the columns and rows, other answers are chosen accordingly so as to solve the puzzle in the least time
possible, as accurately as possible and without the least hints as possible.
 
