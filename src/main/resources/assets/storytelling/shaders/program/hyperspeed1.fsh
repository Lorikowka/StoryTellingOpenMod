#version 150

uniform sampler2D DiffuseSampler;

in vec2 fragUV;
out vec4 fragColor;

uniform vec2 Resolution;
uniform float TimeC;
uniform bool Reversed;

#define TAU 6.28318
#define FLARE 1
#define NUM_SLICES 125.0
const float MAX_SLICE_OFFSET = 0.4;

const float T_PHASE = 5.0;

const float T_JUMP = 0.75;
const float jump_speed = 5.0;
const vec3 blue_col = vec3(0.3, 0.3, 0.5);
const vec3 white_col = vec3(0.85, 0.85, 0.9);
const vec3 flare_col = vec3(0.9, 0.9, 1.4);

float rand(vec2 co) {
    return fract(sin(dot(co.xy, vec2(12.9898, 78.233))) * 43758.5453);
}

float sdLine(in vec2 p, in vec2 a, in vec2 b, in float ring) {
    vec2 pa = p - a, ba = b - a;
    float h = clamp(dot(pa, ba) / dot(ba, ba), 0.0, 1.0);
    return length(pa - ba * h) - ring;
}

vec3 lensflare(vec3 uv, vec3 pos, float flare_size, float ang_offset) {
    float z = uv.z / length(uv.xy);
    vec2 main = uv.xy - pos.xy;
    float dist = length(main);
    float num_points = 2.71;
    float disk_size = 0.2;
    float inv_size = 1.0 / flare_size;
    float ang = atan(main.y, main.x) + ang_offset;
    float fade = (z < 0.0) ? -z : 1.0;

    float f0 = 1.0 / (dist * inv_size + 1.0);
    f0 += f0 * (0.1 * sin((sin(ang * 2.0 + pos.x) * 4.0 - cos(ang * 3.0 + pos.y)) * num_points) + disk_size);
    if (z < 0.0)
    return clamp(mix(vec3(f0), vec3(0.0), 0.75 * fade), 0.0, 1.0);
    else
    return vec3(f0);
}

vec3 cc(vec3 color, float factor, float factor2) {
    float w = color.x + color.y + color.z;
    return mix(color, vec3(w) * factor, w * factor2);
}
//vec3 hsv2rgb(vec3 c) {
//    vec4 K = vec4(1.0, 2.0/3.0, 1.0/3.0, 3.0);
//    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
//    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
//}

void main() {

    vec2 fragCoord = fragUV * Resolution;
    vec2 uv = (fragCoord / Resolution) * 2.0 - 1.0;


    vec2 p = (2.0 * fragCoord.xy - Resolution.xy) / min(Resolution.x, Resolution.y);
    vec3 v = vec3(p, 1.0);
    float p_len = length(p);
    vec3 color = vec3(0.0);

    float time = clamp(TimeC, 0.0, T_PHASE);
    float t;

    //еще и эффеткт если плющить
    //    vec2 rawUV = (fragCoord / Resolution) * 2.0 - 1.0;
    //    float t03 = smoothstep(0.75, 1.0, t);
    //    float distortion1 = mix(0.0, 0.2, smoothstep(0.0, T_JUMP + 0.2, t));
    //    distortion1 += 50.0 * pow(t03, 4.0);
    //    float r1 = length(rawUV);
    //    vec2 uv = rawUV * (1.0 + distortion1 * r1 * r1);
    //
    //    vec2 p = uv * Resolution / min(Resolution.x, Resolution.y);

    if (!Reversed) {
        t = time / T_PHASE;
    } else {
        t = 1.0 - (time / T_PHASE);
    }

    float fade = clamp(mix(0.1, 1.1, t * 2.0), 0.0, 2.0);

    for (float i = 0.0; i < 80.0; i++) {
        vec3 trail_color = vec3(0.0);

        float angle = atan(v.y, v.x) / 3.141592 / 2.0 + 0.13 * i;
        float slice = floor(angle * NUM_SLICES);
        float slice_fract = fract(angle * NUM_SLICES);

        float slice_offset = MAX_SLICE_OFFSET * rand(vec2(slice, 4.0 + i * 25.0)) - (MAX_SLICE_OFFSET / 2.0);
        float dist = 10.0 * rand(vec2(slice, 1.0 + i * 10.0)) - 5.0;
        float z = dist * v.z / length(v.xy);
        float f = sign(dist); if (f == 0.0) f = 1.0;
        float fspeed = f * (0.1 * rand(vec2(slice, 1.0 + i * 10.0)) + i * 0.01);
        float fjump_speed = f * jump_speed;

        float trail_start = 10.0 * rand(vec2(slice, 0.0 + i * 10.0)) - 5.0;
        float accel = pow(t, 3.0);
        trail_start -= fjump_speed * accel;
        float trail_end = trail_start - t * fspeed;

        float trail_x = smoothstep(trail_start, trail_end, z);

        //        float hue = rand(vec2(slice, i * 7.123));
        //        vec3 hsv = vec3(hue, 0.8, 1.0); // насыщенность и яркость можно настраивать
        //        vec3 rgb_col = hsv2rgb(hsv);
        //
        //        vec3 start_col = Reversed ? white_col : rgb_col;
        //        vec3 end_col = Reversed ? rgb_col : white_col;

        vec3 start_col = Reversed ? white_col : blue_col;
        vec3 end_col = Reversed ? blue_col : white_col;
        trail_color = mix(start_col, end_col, trail_x);

        float h = sdLine(
            vec2(slice_fract + slice_offset, z),
            vec2(0.5, trail_start),
            vec2(0.5, trail_end),
            mix(0.0, 0.015, t * z));

        float threshold = 0.09;
        h = (h < 0.01) ? 1.0 : 0.85 * smoothstep(threshold, 0.0, abs(h));

        trail_color *= fade * h;
        color = max(color, trail_color);
    }

    #ifdef FLARE
    float flare_size = mix(0.0, 0.1, smoothstep(0.35, T_JUMP + 0.2, t));
    float t01 = smoothstep(T_JUMP, 1.0, t);
    flare_size += 20.0 * pow(t01, 4); // 2-4

    vec3 flare = flare_col * lensflare(v, vec3(0.0), flare_size, t);
    color += cc(flare, 0.5, 0.1);
    color += mix(0.0, 1.0, smoothstep(T_JUMP + 0.1, 1.0, t));
    #else
    color += mix(0.0, 1.0, smoothstep(T_JUMP - 0.0, 1.0, t));
    #endif

    vec2 rawUV = (fragCoord / Resolution) * 2.0 - 1.0;
    float r = length(rawUV);
    vec2 dir = normalize(rawUV);

    float jumpProgress = clamp(t / 1.0, 0.0, 1.0);
    float jumpT = pow(jumpProgress, 3.0);

    float burstT = smoothstep(0.6, 1.0, t);


    float distortion = 0.3 * jumpT + 0.7 * pow(burstT, 4.0);
    vec2 distortedUV = rawUV - dir * distortion * r;
    vec2 texUV = distortedUV * 0.5 + 0.5;
    texUV = clamp(texUV, 0.0, 1.0);

    vec4 baseColor = texture(DiffuseSampler, texUV);
    fragColor = baseColor + vec4(color, clamp(length(color), 0.0, 1.0));
}