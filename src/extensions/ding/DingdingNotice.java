package extensions.ding;

import basetool.Sound;

/**
 * @author linxi
 * www.leftvalue.top
 * extensions.ding
 * Date 2018/8/16 5:23 PM
 */
class DingdingNotice extends AbstractNotice {
    @Override
    public AbstractNotice setMessage(String msg) {
        return super.setMessage(msg);
    }

    @Override
    public void run() {
        Sound sound = new Sound();
        for (int i = 0; i < 3; i++) {
            sound.iphone();
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
    }
}

