# AsynProg-Pr3
Task1.java - завдання №1. Task2.java - завдання №2. Всі інші файли(.jpg, .png) - створенні для завдання №2.

1. «Заміряйте час на виконання обох версій (програмно). Чи побачили Ви різницю?»
  Результат з малою кількістю елементів в масиві(виведення масиву не вставляла, щоб не займати місце):
  Введіть кількість елементів масиву: 600
  Введіть початкове значення елементів: 1
  Введіть кінцеве значення елементів: 9
  Згенерований масив:
  ThreadPool (Work Dealing):
  Сума попарних елементів: 5899
  Час виконання: 5 мс
  ForkJoinPool (Work Stealing):
  Сума попарних елементів: 5899
  Час виконання: 3 мс
  
  Результат з великою кількістю елементів в масиві(виведення масиву не вставляла, щоб не займати місце):
  Введіть кількість елементів масиву: 30000
  Введіть початкове значення елементів: 1
  Введіть кінцеве значення елементів: 4
  Згенерований масив:
  ThreadPool (Work Dealing):
  Сума попарних елементів: 150024
  Час виконання: 4 мс
  ForkJoinPool (Work Stealing):
  Сума попарних елементів: 150024
  Час виконання: 5 мс
  
  З 600 елементами швидше працює Work Stealing. Масив розділяється на достатньо малі підзадачі, що дозволяє Work Stealing працювати ефективно, і тому час виконання може бути меншим. Work Dealing може не повною мірою використовувати переваги паралелізму, оскільки накладні витрати на розподіл задач і управління потоками можуть бути помітними. Ці витрати можуть включати час на створення потоків і їх ініціалізацію, а також на координацію результатів між потоками.
  
  З 30000 швидше - Work Dealing. Work Dealing має менше накладних витрат, оскільки не виконується рекурсивне розбиття задач, і кожен потік працює з великою частиною масиву.

2. «Обрали тому що легший в реалізації чи тому що краще, швидше виконує завдання?»
   Перше - простота реалізації.
Друге - Якщо кількість файлів і директорій дуже велика, деякі потоки можуть завершити свою роботу раніше за інші. Work Stealing дозволяє цим потокам отримати нові підзадачі і продовжити обробку, що дозволяє скоротити загальний час виконання програми.
