package com.teamresourceful.resourcefullib.common.yabn.base.primitives;

import com.teamresourceful.resourcefullib.common.yabn.base.YabnType;

public interface PrimitiveContents {

    YabnType getId();

    byte[] toData();
}
