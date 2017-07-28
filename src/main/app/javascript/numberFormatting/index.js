
export function formatBytes(count) {
    let result = ''

    const abscount = Math.abs(count)
    if (abscount >= 1024) {
      const units =  ['K', 'M', 'G', 'T', 'P', 'E', 'Z', 'Y']
      const i = Math.floor(Math.log2(abscount)/10)
      if (i < units.length + 1) {
        result = (count / Math.pow(1024, i)).toFixed(1) + ' ' + units[i-1] + 'iB';
      } else {
        result = count.toExponential(2) + ' bytes'
      }
    }
    else {
      result = count + ' bytes';
    }
    return result;
}
