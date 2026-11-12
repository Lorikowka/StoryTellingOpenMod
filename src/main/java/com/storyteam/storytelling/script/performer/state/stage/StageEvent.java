package com.storyteam.storytelling.script.performer.state.stage;

import com.storyteam.storytelling.script.performer.Performer;

public record StageEvent<P extends Performer<P>>(String name, P host) {
}
