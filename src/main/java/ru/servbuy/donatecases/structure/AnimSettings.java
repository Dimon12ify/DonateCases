package ru.servbuy.donatecases.structure;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Sound;

public class AnimSettings {
    @Getter @Setter
    private Sound rotateSound;
    @Getter @Setter
    private Sound endSound;
    @Getter @Setter
    private double speed;
    @Getter @Setter
    private double particlesSpeed;
    @Getter @Setter
    private int rotateTime;
    @Getter @Setter
    private double radius;
    @Getter @Setter
    private boolean frostlordUse;

    public AnimSettings(Sound rotateSound, Sound endSound, double speed, double particlesSpeed, int rotateTime,
                        double radius, boolean frostlordUse) {
        this.rotateSound = rotateSound;
        this.endSound = endSound;
        this.speed = speed;
        this.particlesSpeed = particlesSpeed;
        this.rotateTime = rotateTime;
        this.radius = radius;
        this.frostlordUse = frostlordUse;
    }
}
