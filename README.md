# financeAndroidApp
This project was created by Matheus Pereira and Anna Boccuzzi for Bo Sheng's Mobile Applications class at the University of Massachusetts Boston Fall semester 2021. 
To start the app you can create a profile with name, valid email, and matching passwords of at least six character. 
There is a login saved in the database that can be used instead of creating a new profile:    email: f@live.com     password: asdasd

After the user signs in they are brought to the next activity with a tab layout. The tabs on the top horizontal bar are scrollable left and right with a total of four tabs.
The first tab offers the user a transactions record where they can click the green plus button to add income, popping up a window to enter the $ amount, type, and a note. 
The same applies for expenses, and they are reflected as a list upon clicking save in the corner of the pop up window. 
The expenses and incomes are calculated for a sum total at the top of the page reflected with green for income and red for expense. 
The next tab is for the user to create a saving goal and calculate how many months it will take to achieve that goal. This tab takes three inputs, the goal amount to be entered, 
which if it is a thousand or more should be formatted without commas, the amount the user has already saved, and the intended monthly contribution from the user. Upon clicking
the 'OK' button the pie chart is animated with the users inputs and the months until the goal will be reached is displayed at the bottom of the screen. The next tab is the 
Billing Cycles tab. This tab features a compactCalendarView with buttons on the top left and right corners to advance the month and go to previous months. The user then can 
enter a day in the format of the first three letters of the month starting with a capital, (Ex: Jan Feb Mar Apr May Jun Jul Aug Sep Oct Nov Dec). Below this the user enters 
the day in a two digit number format (Ex: 01 02 09 10), as well as the year below that in a four digit format (Ex: 2021). When the user clicks "ADD" the date is marked on the 
calendar with a small cyan blue circle under the date. Up to three events can be added to the same day. Clicked on days are temporarily highlighted in purple, and the current 
day is highlighted in blue. The last tab is for Coupons, the user has four icons presented to them all of which have a functional click element that opens a popular coupon 
website in the phone's browser. 
