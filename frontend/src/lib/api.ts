const toKR = (value: number | undefined, digits = 8) => {
    return value?.toLocaleString('ko-KR', { maximumFractionDigits: digits }) ?? 0;
}

const checkNaN = (value: number) => {
    return isNaN(value) ? 0 : value
}

export { toKR, checkNaN }