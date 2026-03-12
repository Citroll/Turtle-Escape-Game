package ca.sfu.cmpt276.turtleescape.enemy;

import ca.sfu.cmpt276.turtleescape.UI.GamePanel;
import ca.sfu.cmpt276.turtleescape.entity.Entity;

import java.util.Random;

public class ENEMY_seagull extends Entity {

    public ENEMY_seagull(GamePanel gp) {
        super(gp);

        name = "Little Kid";
        type = 1;

        speed = 1;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage(){
        up1 = setUp("enemies/seagullUp");
        up2 = setUp("enemies/seagullUp");
        down1 = setUp("enemies/seagullDown");
        down2 = setUp("enemies/seagullDown");
        left1 = setUp("enemies/seagullLeft");
        left2 = setUp("enemies/seagullLeft");
        right1 = setUp("enemies/seagullRight");
        right2 = setUp("enemies/seagullRight");
    }

    @Override
    public void setAction() {
        actionLockCounter++;

        if(actionLockCounter == 120) {

            Random random = new Random();
            int i = random.nextInt(100) + 1;

            if (i < 25) {
                direction = "up";
            }
            if (i > 25 && i <= 50) {
                direction = "down";
            }
            if (i > 50 && i <= 75) {
                direction = "left";
            }
            if (i > 75 && i <= 100) {
                direction = "right";
            }

            actionLockCounter = 0;
        }
    }

}
