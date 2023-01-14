# Classes:

## Sensors: 

Imported by Motors or Robot

### Limelight

**Computer vision and interaction (either in as an instance that tracks itself or a set of static functions that interact with vision)**

- Get distance from set target (each axis)
- Get angle from set target (uses previous plus TRIG)
- Draw interesting graphics based on points of interest with circles on smartboard?

### Gyroscope/Accel.

**Duh**

- What rotation are we on
- What speed are we going
- Check speed over time

## Moto:

Imported by Robot (probably)

**Methods to deal with the various points of actuation/movement throughout the robot (perhaps imports computer vision directly so that it can automatically make movements based on method calls)**

### Driving

- Move distance
- Rotate distance
- Move until told to stop
- Rotate until told to stop
- Auto align to tag (using limelight)
  - Return false if not found
- Auto align to tape (using limelight)
  - Return false if not found
- Stay on platform with gyroscope and vision
  - Launch (as a bit)

### Arm/Claw

- Move distance
- Move until told to stop
- Grab
- Let go
- Grab position
- Upper score
- Middle score
- Lower score
- EXTEND
- RETRACT

## Constants:

Imported as needed

**Barely even a class, just a place to store static constants to be imported and used**

Add as needed

## Joystick:

Imported by Robot (probably)

**Uses defined variables or config file to bind joystick controls to variables related to robot action. Ideally can handle toggling, one time presses, and other stuff.**

- Check single action button
- Check multi action button
- Check joystick
  - Ability to add modifier
- Check trigger
  - Ability to add modifier 
- Combo buttons (as a bit)
- Add mappings based on config

# TODO:

### Programming

- [ ] Planning
  - [x] Fix Erin's editor
  - [x] Install Markdown editor extension on Erin's editor and explain
  - [ ] Finalize TODO
- [ ] Pre-Robot Code
  - [ ] Decide methods for each class (can be changed later)
  - [ ] Create config file
  - [ ] Translate methods into code with empty methods
  - [ ] Write code for moment using joystick class and configurable motor controllers
  - [ ] Document all the methods using comments
- [ ] Post-Robot Code
  - [ ] Fill out the rest of the methods already declared using finalized concept
  - [ ] Write code for the grabber/whatever other stuff they throw in there
  - [ ] Write autonomous 
  - [ ] Test

### Electronics

- [ ] Planning
  - [ ] Estimate number of motors and motor controllers needed/which ones 
  - [ ] Design inital board (will be thrown out of window later, but it's nice to have an inital design to jump off from)
  - [ ] Test batteries ASAP
  - [ ] Finalize TODO
- [ ] Post Robot Design/Building
  - [ ] Design final board
  - [ ] Fabricate final board
  - [ ] Get sad when they change the parameters for final board
  - [ ] Redesign final board
  - [ ] Refabricate final board
  - [ ] Run wires as needed
  - [ ] Setup limelight
  - [ ] Test

# Resources:

- FRC Resources
  - https://github.wpilib.org/allwpilib/docs/release/java/index.html
  - https://codedocs.revrobotics.com/java/com/revrobotics/package-summary.html
  - https://docs.ctre-phoenix.com/en/stable/ch05a_CppJava.html
  - https://api.ctr-electronics.com/phoenix/release/java/
- For config file 
  - https://www.baeldung.com/java-properties
  - https://docs.oracle.com/javase/7/docs/api/java/util/Properties.html
