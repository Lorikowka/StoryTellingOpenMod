#version 150

in vec2 fragUV;
out vec4 fragColor;

uniform sampler2D Sampler0;
uniform vec2 iResolution;
uniform float iTime;

uniform float glitchAmplitude;
uniform float glitchNarrowness;
uniform float glitchBlockiness;
uniform float glitchMinimizer;

float rand(vec2 p) {
    float t = floor(iTime * 20.) / 10.;
    return fract(sin(dot(p, vec2(t * 12.9898, t * 78.233))) * 43758.5453);
}

float noise(vec2 uv, float blockiness) {
    vec2 lv = fract(uv);
    vec2 id = floor(uv);

    float n1 = rand(id);
    float n2 = rand(id + vec2(1, 0));
    float n3 = rand(id + vec2(0, 1));
    float n4 = rand(id + vec2(1, 1));

    vec2 u = smoothstep(0.0, 1.0 + blockiness, lv);

    return mix(mix(n1, n2, u.x), mix(n3, n4, u.x), u.y);
}

float fbm(vec2 uv, int count, float blockiness, float complexity) {
    float val = 0.0;
    float amp = 0.5;

    while (count != 0) {
        val += amp * noise(uv, blockiness);
        amp *= 0.5;
        uv *= complexity;
        count--;
    }

    return val;
}

void main() {
    vec2 uv = fragUV;
    vec2 res = iResolution;
    vec2 uv2 = vec2(uv.x * res.x / res.y, exp(uv.y));

    float shift = glitchAmplitude * pow(fbm(uv2, 4, glitchBlockiness, glitchNarrowness), glitchMinimizer);

    float colR = texture(Sampler0, vec2(uv.x + shift, uv.y)).r * (1. - shift);
    float colG = texture(Sampler0, vec2(uv.x - shift, uv.y)).g * (1. - shift);
    float colB = texture(Sampler0, vec2(uv.x - shift, uv.y)).b * (1. - shift);

    vec3 f = vec3(colR, colG, colB);

    fragColor = vec4(f, 1.);
}
