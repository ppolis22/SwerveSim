# SwerveSim
A sandbox to play with swerve control logic while awaiting actual hardware.

It's worth noting a few issues with the current iteration:
- There's no output scaling to keep the drive vector magnitude between [-1, 1], I'm thinking this will likely involve scaling all wheel outputs proportionately
- If the stick inputs are set relatively high you'll see the wheels start to drift away from their square formation. This is because some wheels will take longer to turn to their goal angles, while others will happen to be closer to their goals to start with. This is kinda tough to visualize, but I imagine is something we'll have to come through on the actual robot code as well to avoid brown-outs when wheels are momentarily pulling in conflicting directions.
- Yeah it drives off the screen after like 2 seconds, sue me
