#version 150

in vec2 fragUV;
out vec4 fragColor;

uniform vec2 pos1;
uniform vec2 pos2;
uniform vec2 size;
uniform vec4 color;
uniform float thickness;
uniform float smoothness;

float sdSegment(vec2 p, vec2 a, vec2 b) {
    vec2 pa = p - a;
    vec2 ba = b - a;
    float h = clamp(dot(pa, ba) / dot(ba, ba), 0.0, 1.0);
    return length(pa - ba * h);
}

void main() {
    vec2 p = fragUV * size;
    float halfT = thickness * 0.5;

    float d = sdSegment(p, pos1, pos2);
    float alpha = 1.0 - smoothstep(halfT - smoothness, halfT + smoothness, d);

    fragColor = vec4(color.rgb, color.a * alpha);
}
