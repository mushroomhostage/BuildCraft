package buildcraft.api;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TileNetworkData
{
    int DEFAULT = 0;
    int UNSIGNED_BYTE = 1;
    int UNSIGNED_SHORT = 2;

int staticSize() default -1;

int intKind() default 0;
}
