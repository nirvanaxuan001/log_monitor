# encoding: UTF-8
import re
def re_tools(pattern,content):
    _pattern = re.compile(pattern)
    _match = _pattern.match(content)
    if _match:
        return _match.groups()
    else:
        return None


