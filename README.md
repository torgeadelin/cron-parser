# ⏳cron-parser

Command line application / script which parses a cron string and expands each field to show the times at which it will run.

## 🛠 Build And Compile

1. Install [gradle](https://gradle.org/) if you don't have it already
2. Clone the project using `git clone`
3. Go to the project directory `cd cron-parser`
4. Run `gradle compile` in your terminal to compile the code
5. Run `gradle test` to run unit tests
6. Run `gradle fatJar` to generate a jar file that you can run (see it in `build/lib`)

## 🏃 Run

If you'd like to run the app, use

`java -jar build/lib/cron-all.jar "arg1" "arg2"`

Note that **arg1** and **arg2** are **mandatory**.

- arg1 should be a string with the following format: `minute, hour, dayMonth, month, dayWeek` where minute, hour, etc can have different kinds of formats. See below.

- arg2 should be a command ex `/usr/bin/find`

**Example**

- `java -jar build/lib/cron-all.jar "*/2 */2 */2 */2 */2" "/find"`
  Will run `/find` at every 2nd minute past every 2nd hour on every 2nd day-of-month if it's on every 2nd day-of-week in every 2nd month.

- `java -jar build/lib/cron-all.jar "15 0 1,15 * 1-5" "/find"`
  Will run `/find` at every 15th minute past hour 0 on day-of-month 1 and 15 and on every day-of-week from Monday through Friday.

## 📕 Cron time fields format

_Valid for all fields, time, minute etc_

`n` - return the specific unit of time, ex "1 1 1 1 1" "/comand" => Runs `/comand` At 01:01 on day-of-month 1 and on Monday in January.
`n-m` - returns a range from n to m
`*/n` - returns a step sequence with multiples of n, ex "\*/10" for the minute field would translate to "10 20 30 40 50" that is 10 minutes past, 20 minutes past, etc..
`n,m,p` - returns a sequence of numbers "n m q"
